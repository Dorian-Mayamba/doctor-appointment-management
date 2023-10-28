package co.ac.uk.doctor.repositories;

import co.ac.uk.doctor.entities.Appointment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppointmentRepository extends CrudRepository<Appointment, Long> {
    Optional<Appointment> getAppointmentByDoctorIdAndPatientId(Long doctorId, Long patientId);
}
