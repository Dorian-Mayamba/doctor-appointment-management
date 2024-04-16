package co.ac.uk.doctor.services;

import co.ac.uk.doctor.entities.Appointment;
import co.ac.uk.doctor.entities.Doctor;
import co.ac.uk.doctor.entities.Patient;
import co.ac.uk.doctor.services.generic.IUserDetailsService;
import co.ac.uk.doctor.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientDetailsService patientDetailsService;
    private final DoctorDetailsService doctorDetailsService;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository,
                              @Qualifier("createDoctorDetailsService") IUserDetailsService<Doctor>  doctorDetailsService,
                              @Qualifier("createPatientDetailsService") IUserDetailsService<Patient> patientDetailsService){
        this.appointmentRepository = appointmentRepository;
        this.doctorDetailsService = (DoctorDetailsService) doctorDetailsService;
        this.patientDetailsService = (PatientDetailsService) patientDetailsService;
    }

    public Appointment saveAppointment(Long doctorId, Long patientId, LocalDate date, LocalTime time, String title){
        Doctor doctor = (Doctor) doctorDetailsService.loadUserById(doctorId);
        Patient patient = (Patient) patientDetailsService.loadUserById(patientId);
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setTime(time);
        appointment.setTitle(title);
        appointment.setEndTime(time.plusHours(1));
        appointment.setDate(date);
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAppointments() {
        return this.appointmentRepository.findAll();
    }
}
