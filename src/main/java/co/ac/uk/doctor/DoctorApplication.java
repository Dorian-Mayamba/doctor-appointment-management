package co.ac.uk.doctor;

import co.ac.uk.doctor.repositories.RoleRepository;
import co.ac.uk.doctor.seeders.AdminSeeder;
import co.ac.uk.doctor.seeders.DoctorSeeder;
import co.ac.uk.doctor.seeders.PatientsSeeder;
import co.ac.uk.doctor.seeders.RolesSeeder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class DoctorApplication {
    public static void main(String[] args) {
        SpringApplication.run(DoctorApplication.class, args);
    }

    @Bean
    CommandLineRunner roleSeeder(){
        return new RolesSeeder();
    }

    @Bean
    CommandLineRunner DoctorSeeder(){
        return new DoctorSeeder();
    }

    @Bean CommandLineRunner patientSeeder(){
        return new PatientsSeeder();
    }

    @Bean
    CommandLineRunner adminSeeder(){
        return new AdminSeeder();
    }

}
