package co.ac.uk.doctor.configs;

import co.ac.uk.doctor.providers.DoctorAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;

@Configuration
public class ApplicationConfig {
    @Bean
    public AuthenticationProvider createAuthenticationProvider(){
        return new DoctorAuthenticationProvider();
    }
}
