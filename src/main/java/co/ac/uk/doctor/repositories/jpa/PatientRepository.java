package co.ac.uk.doctor.repositories.jpa;

import co.ac.uk.doctor.entities.jpa.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends CrudRepository<Patient, Long> {
    Optional<Patient> getPatientByPatientEmail(String patientEmail);
}
