package co.ac.uk.doctor.configs;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "co.ac.uk.doctor.repositories.jpa")
public class RepositoryConfig {
}
