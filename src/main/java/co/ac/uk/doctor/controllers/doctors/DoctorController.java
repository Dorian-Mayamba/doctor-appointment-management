package co.ac.uk.doctor.controllers.doctors;

import co.ac.uk.doctor.exceptions.AlreadyRegisteredUserException;
import co.ac.uk.doctor.requests.AddDoctorRequest;
import co.ac.uk.doctor.requests.EditDoctorRequest;
import co.ac.uk.doctor.responses.AddDoctorResponse;
import co.ac.uk.doctor.services.DoctorService;
import co.ac.uk.doctor.services.FileStorageServiceImpl;
import co.ac.uk.doctor.entities.Doctor;
import co.ac.uk.doctor.serializers.DoctorSerializer;
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
public class DoctorController {
    private DoctorService doctorService;

    private FileStorageServiceImpl fileStorageService;

    @Autowired
    public DoctorController(DoctorService doctorService,
                            FileStorageServiceImpl fileStorageService) {
        this.doctorService = doctorService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorSerializer>> getDoctors() {
        return ResponseEntity.ok(EntityToSerializerConverter.toDoctorSerializer(doctorService.getUsers()));
    }

    @GetMapping("/doctors/{speciality}")
    public ResponseEntity<List<DoctorSerializer>> findBySpeciality(@PathVariable("speciality") String speciality){
        return ResponseEntity.ok(doctorService.findBySpeciality(speciality));
    }

    @PostMapping("/doctors/create")
    public ResponseEntity<?> createDoctor(@RequestBody AddDoctorRequest addDoctorRequest, Authentication authentication) throws AlreadyRegisteredUserException {
        if (authentication != null){
            Doctor doctor = this.doctorService.addUser(addDoctorRequest);
            return ResponseEntity
                    .ok()
                    .body(new AddDoctorResponse("The doctor "+doctor.getName() + "has been added", true));
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/doctors/update/{doctorId}")
    public ResponseEntity<?> updateDoctor(@RequestBody EditDoctorRequest editDoctorRequest, @PathVariable("doctorId") Long doctorId){
        Doctor updatedDoctor = (Doctor) doctorService.editUser(doctorId,editDoctorRequest);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "The doctor "+ updatedDoctor.getName() + " has been modified");

        return ResponseEntity.ok()
                .body(jsonObject.toString(jsonObject.length()));
    }

    @DeleteMapping("/doctors/delete/{doctorId}")
    public ResponseEntity<?> deleteDoctor(@PathVariable("doctorId") Long doctorId){
        Doctor deletedDoctor = (Doctor) doctorService.removeUser(doctorId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "The doctor "+deletedDoctor.getName() + " has been removed");
        return ResponseEntity.ok()
                .body(jsonObject.toString(jsonObject.length()));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<DoctorSerializer> getDoctor(@PathVariable("doctorId") Long doctorId) {
        Doctor doctor = (Doctor) doctorService.loadUserById(doctorId);
        DoctorSerializer doctorSerializer = EntityToSerializerConverter.toDoctorSerializer(doctor);
        return ResponseEntity
                .ok()
                .body(doctorSerializer);
    }

    @GetMapping("/doctor/{doctorId}/{filename}")
    public ResponseEntity<Resource> loadImage(@PathVariable("doctorId") String doctorId,
                                                    @PathVariable("filename")String filename){
        try{
            Resource file = fileStorageService.load(filename);
            return ResponseEntity
                    .ok(file);
        }catch (Exception ex){
            throw new RuntimeException("could not load file");
        }
    }
}
