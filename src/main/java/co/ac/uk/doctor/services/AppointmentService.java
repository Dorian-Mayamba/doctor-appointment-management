package co.ac.uk.doctor.services;

import co.ac.uk.doctor.entities.jpa.Appointment;
import co.ac.uk.doctor.entities.jpa.Doctor;
import co.ac.uk.doctor.entities.jpa.Patient;
import co.ac.uk.doctor.generic.IUserDetailsService;
import co.ac.uk.doctor.repositories.jpa.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;

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

    public Appointment saveAppointment(Long doctorId,Long patientId, String date, String time){
        Doctor doctor = (Doctor) doctorDetailsService.loadUserById(doctorId);
        Patient patient = (Patient) patientDetailsService.loadUserById(patientId);
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setTime(time);
        appointment.setDate(date);
        return appointmentRepository.save(appointment);
    }
}
