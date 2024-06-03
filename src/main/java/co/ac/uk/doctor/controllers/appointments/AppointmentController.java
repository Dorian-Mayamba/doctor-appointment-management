package co.ac.uk.doctor.controllers.appointments;

import co.ac.uk.doctor.entities.Appointment;
import co.ac.uk.doctor.entities.Doctor;
import co.ac.uk.doctor.entities.Patient;
import co.ac.uk.doctor.requests.UpdateAppointmentRequest;
import co.ac.uk.doctor.responses.AppointmentBookingResponse;
import co.ac.uk.doctor.responses.AppointmentDeleteResponse;
import co.ac.uk.doctor.responses.AppointmentUpdateResponse;
import co.ac.uk.doctor.services.generic.IUserDetailsService;
import co.ac.uk.doctor.requests.AppointmentRequest;
import co.ac.uk.doctor.serializers.AppointmentSerializer;
import co.ac.uk.doctor.services.AppointmentService;
import co.ac.uk.doctor.services.DoctorService;
import co.ac.uk.doctor.services.PatientService;
import co.ac.uk.doctor.utils.EntityToSerializerConverter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final PatientService patientService;
    private final DoctorService doctorService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService,
                                 @Qualifier("createPatientDetailsService") IUserDetailsService<Patient> patientDetailsService,
                                 @Qualifier("createDoctorDetailsService") IUserDetailsService<Doctor> doctorIUserDetailsService) {
        this.appointmentService = appointmentService;
        this.patientService = (PatientService) patientDetailsService;
        this.doctorService = (DoctorService) doctorIUserDetailsService;
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentSerializer>> getAppointment() {
        return ResponseEntity.ok(EntityToSerializerConverter.toAppointmentsSerializer(appointmentService.getAppointments()));
    }

    @PostMapping("/appointments/{doctorId}/{patientId}")
    public ResponseEntity<AppointmentBookingResponse> makeAppointment(
            @PathVariable("doctorId") Long doctorId,
            @PathVariable("patientId") Long patientId,
            @RequestBody AppointmentRequest request) {
        JSONObject jsonObject = new JSONObject();
        Appointment appointment = appointmentService.saveAppointment(doctorId, patientId,
                request.getDate(), request.getTime(), request.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                AppointmentBookingResponse
                        .builder()
                        .message(String.format("Your appointment for %s at %s has been successfully scheduled", appointment.getDate(), appointment.getTime()))
                        .appointment(EntityToSerializerConverter.toAppointmentSerializer(appointment))
                        .build()
        );
    }

    @GetMapping("/appointments/patients/{patientId}")
    public ResponseEntity<List<AppointmentSerializer>> getPatientsAppointments(@PathVariable("patientId") Long patientId) {
        Patient patient = (Patient) patientService.loadUserById(patientId);
        return ResponseEntity.ok(
                EntityToSerializerConverter.toAppointmentsSerializer(patient.getPatientAppointments())
        );
    }

    @GetMapping("/appointments/doctors/{doctorId}")
    public ResponseEntity<List<AppointmentSerializer>> getDoctorAppointments(@PathVariable("doctorId") Long doctorId) {
        Doctor doctor = (Doctor) doctorService.loadUserById(doctorId);
        return ResponseEntity.ok(
                EntityToSerializerConverter.toAppointmentsSerializer(doctor.getDoctorAppointments())
        );
    }

    @PutMapping("/appointments/{appointmentId}/update")
    public ResponseEntity<AppointmentUpdateResponse> updateAppointment(@PathVariable("appointmentId") Long appointmentId, @RequestBody UpdateAppointmentRequest appointmentUpdateRequest){
        return appointmentService.updatePatientAppointment(appointmentId, appointmentUpdateRequest);
    }

    @DeleteMapping("/appointments/{appointmentId}/cancel")
    public ResponseEntity<AppointmentDeleteResponse> cancelAppointment(@PathVariable("appointmentId") Long appointmentId){
        return appointmentService.cancelAppointment(appointmentId);
    }



}
