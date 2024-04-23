package co.ac.uk.doctor.controllers.patient;


import co.ac.uk.doctor.exceptions.AlreadyRegisteredUserException;
import co.ac.uk.doctor.requests.AddPatientRequest;
import co.ac.uk.doctor.requests.EditPatientRequest;
import co.ac.uk.doctor.responses.AddPatientResponse;
import co.ac.uk.doctor.services.FileStorageServiceImpl;
import co.ac.uk.doctor.entities.Patient;
import co.ac.uk.doctor.services.PatientService;
import co.ac.uk.doctor.serializers.PatientSerializer;
import co.ac.uk.doctor.utils.EntityToSerializerConverter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PatientController {
    private PatientService patientService;

    private FileStorageServiceImpl fileStorageService;

    @Autowired
    public PatientController(PatientService patientService, FileStorageServiceImpl fileStorageService){
        this.patientService = patientService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/patients")
    public ResponseEntity<List<PatientSerializer>> getPatients(){
        List<PatientSerializer> patientSerializers =
                patientService.getUsers()
                        .stream()
                        .map((EntityToSerializerConverter::toPatientSerializer))
                        .collect(Collectors.toList());
        return ResponseEntity.ok(patientSerializers);
    }

    @PostMapping("/patients/create")
    public ResponseEntity<AddPatientResponse> createPatient(@RequestBody AddPatientRequest request, Authentication authentication) throws AlreadyRegisteredUserException {
        if(authentication!=null){
            Patient patient = patientService.addUser(request);
            return ResponseEntity
                    .ok(AddPatientResponse.builder()
                            .message("The patient "+patient.getName() + "has been added")
                            .success(true).build());
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/patients/update/{patientId}")
    public ResponseEntity<?> updatePatient(@RequestBody EditPatientRequest editPatientRequest, @PathVariable("patientId") Long patientId){
        Patient updatedPatient = patientService.editUser(patientId, editPatientRequest);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "The doctor "+ updatedPatient.getName() + " has been modified");

        return ResponseEntity.ok()
                .body(jsonObject.toString(jsonObject.length()));
    }

    @DeleteMapping("/patients/delete/{patientId}")
    public ResponseEntity<?> deletePatient(@PathVariable("patientId") Long patientId){
        Patient deletedPatient = patientService.removeUser(patientId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "The patient "+deletedPatient.getName() + " has been removed");
        return ResponseEntity.ok()
                .body(jsonObject.toString(jsonObject.length()));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<PatientSerializer> getPatient(@PathVariable("patientId") Long patientId){
        Patient patient = (Patient) patientService.loadUserById(patientId);
        PatientSerializer patientSerializer = EntityToSerializerConverter.toPatientSerializer(patient);
        return ResponseEntity
                .ok()
                .body(patientSerializer);
    }

    @GetMapping("/patient/{patientId}/{fileName}")
    public ResponseEntity<Resource> loadResource(@PathVariable("patientId")String patientId,
                                                 @PathVariable("fileName") String filename){
        try{
            Resource file = fileStorageService.load(filename);
            return ResponseEntity
                    .ok(file);
        }catch (Exception ex){
            throw new RuntimeException("could not load file");
        }
    }

}
