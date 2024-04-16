package co.ac.uk.doctor.repositories;

import co.ac.uk.doctor.entities.Patient;
import co.ac.uk.doctor.entities.Rating;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface RatingRepository extends ListCrudRepository<Rating, Long> {
    Optional<Rating> findRatingByPatient(Patient patient);
}
