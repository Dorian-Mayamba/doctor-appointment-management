package co.ac.uk.doctor.configs;

import co.ac.uk.doctor.constants.ApplicationConstants;
import co.ac.uk.doctor.filters.JWTFilter;
import co.ac.uk.doctor.generic.IUserDetailsService;
import co.ac.uk.doctor.services.AdminDetailsService;
import co.ac.uk.doctor.services.DoctorDetailsService;
import co.ac.uk.doctor.services.PatientDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.*;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig{
    private JWTFilter jwtFilter;
    private AuthenticationManagerBuilder builder;

    private final AuthenticationProvider authenticationProvider;
    @Autowired
    public AppSecurityConfig(JWTFilter jwtFilter, AuthenticationManagerBuilder builder, AuthenticationProvider provider){
        this.jwtFilter = jwtFilter;
        this.builder = builder;
        this.authenticationProvider = provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf((csrf)->csrf.disable())
                .authorizeHttpRequests((authorize)->authorize
                        .requestMatchers("/user", "/auth/**", "/hello",
                                "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated())
                .securityContext((securityContext)->securityContext
                        .requireExplicitSave(true)
                        .securityContextRepository(new DelegatingSecurityContextRepository(
                                new RequestAttributeSecurityContextRepository(),
                                new HttpSessionSecurityContextRepository()
                        )))
                .logout((logout)->logout
                        .logoutUrl("/auth/logout").permitAll()
                                .deleteCookies(ApplicationConstants.JWT_TOKEN)
                        )
                .sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager createAuthenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(this.authenticationProvider);
        return authenticationManagerBuilder.build();
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
}
