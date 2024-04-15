package co.ac.uk.doctor.repositories;

import co.ac.uk.doctor.userdetails.Doctor;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends ListCrudRepository<Doctor, Long> {
    Optional<Doctor> getDoctorByDoctorEmail(String doctorEmail);
}
