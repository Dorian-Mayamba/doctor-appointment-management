package co.ac.uk.doctor.controllers.doctors;

import co.ac.uk.doctor.services.FileStorageServiceImpl;
import co.ac.uk.doctor.entities.Doctor;
import co.ac.uk.doctor.services.generic.IUserDetailsService;
import co.ac.uk.doctor.serializers.DoctorSerializer;
import co.ac.uk.doctor.utils.EntityToSerializerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
public class DoctorController {
    private IUserDetailsService<Doctor> doctorDetailService;

    private FileStorageServiceImpl fileStorageService;

    @Autowired
    public DoctorController(@Qualifier("createDoctorDetailsService") IUserDetailsService<Doctor> doctorDetailService,
                            FileStorageServiceImpl fileStorageService) {
        this.doctorDetailService = doctorDetailService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/doctors")
    public ResponseEntity<?> getDoctors() {
        List<DoctorSerializer> doctorSerializers =
                doctorDetailService.getUsers()
                        .stream()
                        .map((EntityToSerializerConverter::toDoctorSerializer))
                        .collect(Collectors.toList());
        return ResponseEntity
                .ok(doctorSerializers);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<DoctorSerializer> getDoctor(@PathVariable("doctorId") Long doctorId) {
        Doctor doctor = (Doctor) doctorDetailService.loadUserById(doctorId);
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
