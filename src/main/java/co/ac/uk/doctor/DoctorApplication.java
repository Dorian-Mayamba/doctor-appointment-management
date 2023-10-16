package co.ac.uk.doctor;

import co.ac.uk.doctor.entities.User;
import co.ac.uk.doctor.repositories.RoleRepository;
import co.ac.uk.doctor.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@SpringBootApplication
@Slf4j
public class DoctorApplication {

    @Autowired
    private RoleRepository roleRepository;
    public static void main(String[] args) {
        SpringApplication.run(DoctorApplication.class, args);
    }


    @Bean
    CommandLineRunner commandLineRunner(){
        return (args -> {

        });
    }
}
