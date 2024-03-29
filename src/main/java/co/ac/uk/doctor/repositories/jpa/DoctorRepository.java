package co.ac.uk.doctor.repositories.jpa;

import co.ac.uk.doctor.entities.jpa.Doctor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends ListCrudRepository<Doctor, Long> {
    Optional<Doctor> getDoctorByDoctorEmail(String doctorEmail);
}
