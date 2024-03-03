package co.ac.uk.doctor.configs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableJpaRepositories(basePackages = "co.ac.uk.doctor.repositories.jpa")
@EnableMongoRepositories(basePackages = "co.ac.uk.doctor.repositories.mongo")
public class RepositoryConfig {
}
