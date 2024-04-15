package co.ac.uk.doctor.controllers.patient;

import co.ac.uk.doctor.userdetails.Patient;
import co.ac.uk.doctor.services.generic.IUserDetailsService;
import co.ac.uk.doctor.serializers.PatientSerializer;
import co.ac.uk.doctor.utils.EntityToSerializerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PatientController {
    private IUserDetailsService<Patient> patientDetailService;

    @Autowired
    public PatientController(@Qualifier("createPatientDetailsService") IUserDetailsService<Patient> patientDetailService){
        this.patientDetailService = patientDetailService;
    }

    @GetMapping("/patients")
    public ResponseEntity<List<PatientSerializer>> getPatients(){
        List<PatientSerializer> patientSerializers =
                patientDetailService.getUsers()
                        .stream()
                        .map((EntityToSerializerConverter::toPatientSerializer))
                        .collect(Collectors.toList());
        return ResponseEntity.ok(patientSerializers);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<PatientSerializer> getPatient(@PathVariable("patientId") Long patientId){
        Patient patient = (Patient) patientDetailService.loadUserById(patientId);
        PatientSerializer patientSerializer = EntityToSerializerConverter.toPatientSerializer(patient);
        return ResponseEntity
                .ok()
                .body(patientSerializer);
    }

}
