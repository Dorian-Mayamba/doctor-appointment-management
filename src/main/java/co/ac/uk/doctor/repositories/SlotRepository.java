package co.ac.uk.doctor.repositories;

import co.ac.uk.doctor.entities.Doctor;
import co.ac.uk.doctor.entities.Slot;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

@Repository
public interface SlotRepository extends ListCrudRepository<Slot, Long> {
    List<Slot> findSlotsByDoctorAndMonth(Doctor doctor, Month month);

    Slot findSlotByDoctorAndMonthAndDateAndTime(Doctor doctor, Month month, LocalDate date, LocalTime time);
}
