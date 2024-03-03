package co.ac.uk.doctor.controllers.appointments;

import co.ac.uk.doctor.entities.jpa.Appointment;
import co.ac.uk.doctor.requests.AppointmentRequest;
import co.ac.uk.doctor.serializers.AppointmentSerializer;
import co.ac.uk.doctor.services.AppointmentService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService){
        this.appointmentService = appointmentService;
    }

    @PostMapping("/appointments/{doctorId}/{patientId}")
    public ResponseEntity<String> makeAppointment(
            @PathVariable("doctorId") Long doctorId,
            @PathVariable("patientId") Long patientId,
            @RequestBody AppointmentRequest request){
        JSONObject jsonObject = new JSONObject();
        Appointment appointment = appointmentService.saveAppointment(doctorId,patientId,
                request.getDate(), request.getTime());
        jsonObject.put("message", "The appointment of the date "+ appointment.getDate() + " has been booked");
        return ResponseEntity.ok(jsonObject.toString(jsonObject.length()));
    }
}
