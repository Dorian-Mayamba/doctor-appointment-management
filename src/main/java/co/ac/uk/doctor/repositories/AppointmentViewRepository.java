package co.ac.uk.doctor.repositories;

import co.ac.uk.doctor.entities.AppointmentComposite;
import co.ac.uk.doctor.entities.DoctorAppointmentsView;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;


public interface AppointmentViewRepository extends ListCrudRepository<DoctorAppointmentsView, AppointmentComposite> {

}
