package co.ac.uk.doctor.controllers.doctors;

import co.ac.uk.doctor.userdetails.Doctor;
import co.ac.uk.doctor.services.generic.IUserDetailsService;
import co.ac.uk.doctor.serializers.DoctorSerializer;
import co.ac.uk.doctor.utils.EntityToSerializerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
public class DoctorController {
    private IUserDetailsService<Doctor> doctorDetailService;

    @Autowired
    public DoctorController(@Qualifier("createDoctorDetailsService") IUserDetailsService<Doctor> doctorDetailService) {
        this.doctorDetailService = doctorDetailService;
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
}
