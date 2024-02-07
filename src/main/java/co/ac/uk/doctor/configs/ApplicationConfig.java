package co.ac.uk.doctor.configs;

import co.ac.uk.doctor.generic.IUserDetailsService;
import co.ac.uk.doctor.providers.DoctorAuthenticationProvider;
import co.ac.uk.doctor.services.AdminDetailsService;
import co.ac.uk.doctor.services.DoctorDetailsService;
import co.ac.uk.doctor.services.PatientDetailsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class ApplicationConfig {
    @Bean
    UserDetailsService users(){
        return new InMemoryUserDetailsManager(
                User.withUsername("user")
                        .password("{noop}password")
                        .authorities("app")
                        .build()
        );
    }
}
