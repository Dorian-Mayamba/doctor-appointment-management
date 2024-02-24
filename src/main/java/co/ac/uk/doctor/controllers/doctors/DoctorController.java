package co.ac.uk.doctor.controllers.doctors;

import co.ac.uk.doctor.entities.Doctor;
import co.ac.uk.doctor.generic.IUserDetails;
import co.ac.uk.doctor.generic.IUserDetailsService;
import co.ac.uk.doctor.serializers.DoctorSerializer;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;


@RestController
public class DoctorController {
    private IUserDetailsService doctorDetailService;

    @Autowired
    public DoctorController(@Qualifier("createDoctorDetailsService") IUserDetailsService doctorDetailService) {
        this.doctorDetailService = doctorDetailService;
    }

    @GetMapping("/doctors")
    public ResponseEntity<?> getDoctors() {
        List<IUserDetails> userDetails = doctorDetailService.getUsers();
        List<DoctorSerializer> doctorSerializers = new ArrayList<>();
        for (IUserDetails iUserDetails : userDetails) {
            Doctor d = (Doctor) iUserDetails;
            DoctorSerializer newDoctorSerializer = new DoctorSerializer();
            newDoctorSerializer.setDoctorId(d.getId());
            newDoctorSerializer.setDoctorSpeciality(d.getSpeciality());
            newDoctorSerializer.setDoctorName(d.getDoctorName());
            newDoctorSerializer.setDoctorEmail(d.getDoctorEmail());
            doctorSerializers.add(newDoctorSerializer);
        }
        return ResponseEntity
                .ok(doctorSerializers);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<DoctorSerializer> getDoctor(@PathVariable("doctorId") Long doctorId) {
        Doctor doctor = (Doctor) doctorDetailService.loadUserById(doctorId);
        DoctorSerializer doctorSerializer = new DoctorSerializer();
        doctorSerializer.setDoctorName(doctor.getDoctorName());
        doctorSerializer.setDoctorEmail(doctor.getDoctorEmail());
        doctorSerializer.setDoctorSpeciality(doctor.getSpeciality());
        doctorSerializer.setDoctorId(doctorId);
        return ResponseEntity
                .ok()
                .body(doctorSerializer);
    }
}
