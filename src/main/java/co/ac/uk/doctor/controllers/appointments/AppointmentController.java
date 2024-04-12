package co.ac.uk.doctor.controllers.appointments;

import co.ac.uk.doctor.entities.Appointment;
import co.ac.uk.doctor.entities.Doctor;
import co.ac.uk.doctor.entities.Patient;
import co.ac.uk.doctor.generic.IUserDetailsService;
import co.ac.uk.doctor.requests.AppointmentRequest;
import co.ac.uk.doctor.serializers.AppointmentSerializer;
import co.ac.uk.doctor.services.AppointmentService;
import co.ac.uk.doctor.services.DoctorDetailsService;
import co.ac.uk.doctor.services.PatientDetailsService;
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

    private final PatientDetailsService patientDetailsService;

    private final DoctorDetailsService doctorDetailsService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService,
                                 @Qualifier("createPatientDetailsService") IUserDetailsService<Patient> patientDetailsService,
                                 @Qualifier("createDoctorDetailsService") IUserDetailsService<Doctor> doctorIUserDetailsService) {
        this.appointmentService = appointmentService;
        this.patientDetailsService = (PatientDetailsService) patientDetailsService;
        this.doctorDetailsService = (DoctorDetailsService) doctorIUserDetailsService;
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentSerializer>> getAppointment() {

        return ResponseEntity.ok(EntityToSerializerConverter.toAppointmentsSerializer(appointmentService.getAppointments()));
    }

    @PostMapping("/appointments/{doctorId}/{patientId}")
    public ResponseEntity<String> makeAppointment(
            @PathVariable("doctorId") Long doctorId,
            @PathVariable("patientId") Long patientId,
            @RequestBody AppointmentRequest request) {
        JSONObject jsonObject = new JSONObject();
        Appointment appointment = appointmentService.saveAppointment(doctorId, patientId,
                request.getDate(), request.getTime(), request.getTitle());
        jsonObject.put("message", "The appointment of the date " + appointment.getDate() + " has been booked");
        return ResponseEntity.status(HttpStatus.CREATED).body(jsonObject.toString(jsonObject.length()));
    }

    @GetMapping("/appointments/patients/{patientId}")
    public ResponseEntity<List<Appointment>> getPatientsAppointments(@PathVariable("patientId") Long patientId) {
        Patient patient = (Patient) patientDetailsService.loadUserById(patientId);
        return ResponseEntity.ok(
                patient.getPatientAppointments()
        );
    }

    @GetMapping("/appointments/doctors/{doctorId}")
    public ResponseEntity<List<Appointment>> getDoctorAppointments(@PathVariable("doctorId") Long doctorId) {
        Doctor doctor = (Doctor) doctorDetailsService.loadUserById(doctorId);
        return ResponseEntity.ok(
                doctor.getDoctorAppointments()
        );
    }
}
