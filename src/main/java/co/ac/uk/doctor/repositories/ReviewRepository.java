package co.ac.uk.doctor.repositories;

import co.ac.uk.doctor.entities.Patient;
import co.ac.uk.doctor.entities.Review;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface ReviewRepository extends ListCrudRepository<Review, Long> {
    Optional<Review> findReviewByPatient(Patient patient);
}
