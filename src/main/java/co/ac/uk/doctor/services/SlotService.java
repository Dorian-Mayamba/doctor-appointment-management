package co.ac.uk.doctor.services;

import co.ac.uk.doctor.entities.Doctor;
import co.ac.uk.doctor.entities.Slot;
import co.ac.uk.doctor.repositories.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

@Service
public class SlotService {

    private final SlotRepository slotRepository;

    @Autowired
    public SlotService(SlotRepository slotRepository){
        this.slotRepository = slotRepository;
    }

    public List<Slot> getDoctorSlots(Doctor doctor){
        return doctor.getSlots();
    }

    public Slot findById(Long slotId){
        return slotRepository.findById(slotId).orElse(null);
    }

    public Slot findDoctorSlot(Doctor doctor, Month month, LocalDate date, LocalTime time){
        return slotRepository.findSlotByDoctorAndMonthAndDateAndTime(doctor,month,date,time);
    }

    public Slot addDoctorSlot(Doctor doctor, Month month, LocalDate date, LocalTime time){
        Slot slot = Slot.builder()
                .doctor(doctor)
                .date(date)
                .time(time)
                .month(month)
                .build();
        return slotRepository.save(slot);
    }

    public Slot updateDoctorSlot(Long slotId, LocalDate date, LocalTime time){
        Slot slot = findById(slotId);
        assert slot != null : "Slot cannot be null";
        slot.setTime(time);
        slot.setDate(date);
        Slot updatedSlot = slotRepository.save(slot);
        return updatedSlot;
    }

    public Slot removeDoctorSlot(Long slotId){
        Slot slot = findById(slotId);
        assert slot != null : "Slot cannot be null";
        slotRepository.delete(slot);
        return slot;
    }

    public List<Slot> getDoctorSlotsByMonth(Doctor doctor, Month month){
        return slotRepository.findSlotsByDoctorAndMonth(doctor, month);
    }

}
