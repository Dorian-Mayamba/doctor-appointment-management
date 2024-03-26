package co.ac.uk.doctor.repositories.jpa;

import co.ac.uk.doctor.entities.jpa.Appointment;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppointmentRepository extends ListCrudRepository<Appointment, Long> {
    Optional<Appointment> getAppointmentByDoctorIdAndPatientId(Long doctorId, Long patientId);
}
