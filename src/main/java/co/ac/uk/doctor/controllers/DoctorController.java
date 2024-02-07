package co.ac.uk.doctor.controllers;

import co.ac.uk.doctor.entities.Doctor;
import co.ac.uk.doctor.generic.IUserDetails;
import co.ac.uk.doctor.generic.IUserDetailsService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DoctorController {
    private IUserDetailsService doctorDetailService;

    @Autowired
    public DoctorController(@Qualifier("createDoctorDetailsService") IUserDetailsService doctorDetailService){
        this.doctorDetailService = doctorDetailService;
    }

    @GetMapping("/Doctors")
    public ResponseEntity<?> getDoctors(){
        List<IUserDetails> userDetails = doctorDetailService.getUsers();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("doctors", userDetails);
        return ResponseEntity
                .ok()
                .body(jsonObject.toString(jsonObject.length()));
    }

    @GetMapping("/Doctor/{doctorId}")
    public ResponseEntity<?> getDoctor(@PathVariable("doctorId") Long doctorId){
       Doctor doctor = (Doctor) doctorDetailService.loadUserById(doctorId);
       JSONObject jsonObject = new JSONObject();
       jsonObject.put("doctor", doctor);
       return ResponseEntity
               .ok()
               .body(jsonObject.toString(jsonObject.length()));
    }
}
