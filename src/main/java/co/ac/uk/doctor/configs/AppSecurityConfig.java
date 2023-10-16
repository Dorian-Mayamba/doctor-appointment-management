package co.ac.uk.doctor.configs;

import co.ac.uk.doctor.constants.ApplicationConstants;
import co.ac.uk.doctor.filters.JWTFilter;
import co.ac.uk.doctor.services.DoctorUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.session.SessionManagementFilter;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig{
    private JWTFilter jwtFilter;
    private AuthenticationManagerBuilder builder;
    @Autowired
    public AppSecurityConfig(JWTFilter jwtFilter, AuthenticationManagerBuilder builder){
        this.jwtFilter = jwtFilter;
        this.builder = builder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf((csrf)->csrf.disable())
                .authorizeHttpRequests((authorize)->authorize
                        .requestMatchers("/user", "/auth/register", "/auth/login").permitAll()
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
                .addFilterBefore(jwtFilter, SessionManagementFilter.class);
        return http.build();
    }

    @Bean
    public SecurityContextHolderStrategy createSecurityContextHolderStrategy(){
        return SecurityContextHolder.getContextHolderStrategy();
    }

    @Bean
    public AuthenticationManager createAuthenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService createUserDetailsService(){
        return new DoctorUserDetailsService();
    }

    @Bean
    PasswordEncoder createPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
