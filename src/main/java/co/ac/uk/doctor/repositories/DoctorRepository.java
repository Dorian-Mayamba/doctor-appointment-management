package co.ac.uk.doctor.repositories;

import co.ac.uk.doctor.entities.Doctor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends CrudRepository<Doctor, Long> {
    Optional<Doctor> getDoctorByDoctorEmail(String doctorEmail);
}
