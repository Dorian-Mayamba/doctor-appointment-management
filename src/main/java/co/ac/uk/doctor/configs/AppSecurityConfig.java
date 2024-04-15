package co.ac.uk.doctor.configs;

import co.ac.uk.doctor.constants.RoleConstants;
import co.ac.uk.doctor.entrypoints.DoctorAuthenticationEntryPoint;
import co.ac.uk.doctor.entrypoints.DoctorBearerTokenAccessDeniedHandler;
import co.ac.uk.doctor.services.generic.IUserDetailsService;
import co.ac.uk.doctor.providers.DoctorAuthenticationProvider;
import co.ac.uk.doctor.services.AdminDetailsService;
import co.ac.uk.doctor.services.DoctorDetailsService;
import co.ac.uk.doctor.services.PatientDetailsService;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.*;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig{
    @Value("${jwt.private.key}")
    RSAPrivateKey privateKey;

    @Value("${jwt.public.key}")
    RSAPublicKey publicKey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf((csrf)->csrf.disable())
                .authorizeHttpRequests((authorize)->authorize
                        .requestMatchers("/user", "/auth/register","/auth/login","/profile/uploads/**",
                                "doctors/**","doctor/**","patient/**","/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/token").permitAll()
                        .requestMatchers("/auth/admin/**").hasAuthority("SCOPE_"+RoleConstants.ADMIN)
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .oauth2ResourceServer(jwt->jwt.jwt(Customizer.withDefaults()))
                .sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions->exceptions.
                        authenticationEntryPoint(new DoctorAuthenticationEntryPoint())
                        .accessDeniedHandler(new DoctorBearerTokenAccessDeniedHandler()));
        return http.build();
    }

    @Bean
    public AuthenticationProvider createAuthenticationProvider(@Qualifier("createAdminDetailsService")IUserDetailsService adminDetailsService,
                                                               @Qualifier("createDoctorDetailsService")IUserDetailsService doctorDetailsService,
                                                               @Qualifier("createPatientDetailsService")IUserDetailsService patientDetailsService,
                                                               PasswordEncoder encoder){
        DoctorAuthenticationProvider provider = new DoctorAuthenticationProvider();
        provider.setAdminDetailsService((AdminDetailsService) adminDetailsService);
        provider.setPasswordEncoder(encoder);
        provider.setDoctorDetailsService((DoctorDetailsService) doctorDetailsService);
        provider.setPatientDetailsService((PatientDetailsService) patientDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager createAuthenticationManager(AuthenticationProvider provider) throws Exception {
        return new ProviderManager(provider);
    }



    @Bean
    public IUserDetailsService createDoctorDetailsService(){
        return new DoctorDetailsService();
    }

    @Bean
    public IUserDetailsService createPatientDetailsService(){
        return new PatientDetailsService();
    }

    @Bean
    public IUserDetailsService createAdminDetailsService(){
        return new AdminDetailsService();
    }

    @Bean
    PasswordEncoder createPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityContextRepository createSecurityContextRepository(){
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(this.publicKey).build();
    }

    @Bean
    JwtEncoder jwtEncoder(){
        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(this.privateKey).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }
}
