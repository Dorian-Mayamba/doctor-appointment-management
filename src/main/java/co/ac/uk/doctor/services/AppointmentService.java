package co.ac.uk.doctor.services;

import co.ac.uk.doctor.entities.Appointment;
import co.ac.uk.doctor.entities.Doctor;
import co.ac.uk.doctor.entities.DoctorAppointmentsView;
import co.ac.uk.doctor.entities.Patient;
import co.ac.uk.doctor.repositories.AppointmentViewRepository;
import co.ac.uk.doctor.requests.UpdateAppointmentRequest;
import co.ac.uk.doctor.responses.AppointmentDeleteResponse;
import co.ac.uk.doctor.responses.AppointmentUpdateResponse;
import co.ac.uk.doctor.serializers.AppointmentSerializer;
import co.ac.uk.doctor.services.generic.IUserDetailsService;
import co.ac.uk.doctor.repositories.AppointmentRepository;
import co.ac.uk.doctor.utils.EntityToSerializerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final AppointmentViewRepository appointmentViewRepository;
    private final PatientService patientService;
    private final DoctorService doctorService;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository,
                              AppointmentViewRepository appointmentViewRepository,
                              @Qualifier("createDoctorDetailsService") IUserDetailsService<Doctor>  doctorDetailsService,
                              @Qualifier("createPatientDetailsService") IUserDetailsService<Patient> patientDetailsService){
        this.appointmentRepository = appointmentRepository;
        this.appointmentViewRepository = appointmentViewRepository;
        this.doctorService = (DoctorService) doctorDetailsService;
        this.patientService = (PatientService) patientDetailsService;
    }

    public Appointment saveAppointment(Long doctorId, Long patientId, LocalDate date, LocalTime time, String title){
        Doctor doctor = (Doctor) doctorService.loadUserById(doctorId);
        Patient patient = (Patient) patientService.loadUserById(patientId);
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setTime(time);
        appointment.setTitle(title);
        appointment.setEndTime(time.plusHours(1));
        appointment.setDate(date);
        appointment.setPatientName(patient.getName());
        appointment.setPatientProfile(patient.getProfile());
        return appointmentRepository.save(appointment);
    }

    public ResponseEntity<List<AppointmentSerializer>> getDoctorAppointments(Long doctorId){
        Doctor doctor = (Doctor) doctorService.loadUserById(doctorId);
        updateAppointment(doctor);
        return ResponseEntity
                .ok()
                .body(EntityToSerializerConverter.toAppointmentsSerializer(doctor.getAppointments()));
    }

    public ResponseEntity<List<AppointmentSerializer>> getPatientAppointments(Long patientId){
        Patient patient = (Patient) patientService.loadUserById(patientId);
        return ResponseEntity
                .ok()
                .body(EntityToSerializerConverter.toAppointmentsSerializer(patient.getAppointments()));
    }

    public List<DoctorAppointmentsView> getAppointments() {
        return this.appointmentViewRepository.findAll();
    }

    public ResponseEntity<AppointmentUpdateResponse> updatePatientAppointment(Long appointmentId, UpdateAppointmentRequest appointmentUpdateRequest) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if(appointment != null){
            appointment.setDate(appointmentUpdateRequest.getDate());
            appointment.setTime(appointmentUpdateRequest.getTime());
            appointment.setStatus(appointmentUpdateRequest.getStatus() != null ? appointmentUpdateRequest.getStatus() : appointment.getStatus());
            appointmentRepository.save(appointment);
            return ResponseEntity.ok(
                    AppointmentUpdateResponse
                            .builder()
                            .message("Successfully updated your appointment")
                            .appointmentSerializer(EntityToSerializerConverter.toAppointmentSerializer(appointment))
                            .build()
            );
        }
        return ResponseEntity
                .badRequest()
                .body(AppointmentUpdateResponse
                        .builder()
                        .message("Could not update this appointment")
                        .build());
    }

    public ResponseEntity<AppointmentDeleteResponse> cancelAppointment(Long appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);

        if(appointment.isPresent()){
            Appointment deletedAppointment = appointment.get();
            appointmentRepository.delete(deletedAppointment);
            AppointmentDeleteResponse appointmentDeleteResponse = AppointmentDeleteResponse
                    .builder()
                    .message(String.format("You have cancelled the %s appointment", deletedAppointment.getDate().toString()))
                    .build();
            return ResponseEntity
                    .ok(appointmentDeleteResponse);
        }
        return ResponseEntity
                .badRequest()
                .body(new AppointmentDeleteResponse("Could not delete your appointment"));
    }

    public void updateAppointment(Doctor doctor){
        for (Appointment appointment:doctor.getAppointments()){
            if(appointment.getPatient() == null){
                appointment.setPatientProfile("default.png");
            }
        }
        doctorService.saveDoctor(doctor);
    }
}
