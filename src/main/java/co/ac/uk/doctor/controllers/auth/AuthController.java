package co.ac.uk.doctor.controllers.auth;
import co.ac.uk.doctor.entities.jpa.Doctor;
import co.ac.uk.doctor.entities.jpa.Patient;
import co.ac.uk.doctor.exceptions.AlreadyRegisteredUserException;
import co.ac.uk.doctor.generic.IUserDetailsService;
import co.ac.uk.doctor.requests.*;
import co.ac.uk.doctor.responses.AddDoctorResponse;
import co.ac.uk.doctor.responses.LoginResponse;
import co.ac.uk.doctor.responses.RegisterResponse;
import co.ac.uk.doctor.services.AuthService;
import co.ac.uk.doctor.services.DoctorDetailsService;
import co.ac.uk.doctor.services.RoleService;
import co.ac.uk.doctor.utils.RoleCheckerUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;

    private final SecurityContextRepository repository;

    private RoleService roleService;

    private IUserDetailsService doctorDetailsService;

    @Autowired
    public AuthController(AuthService authService,
                          SecurityContextRepository repository,
                          RoleService roleService,
                          @Qualifier("createDoctorDetailsService") IUserDetailsService doctorDetailsService){
        this.authService = authService;
        this.repository = repository;
        this.roleService = roleService;
        this.doctorDetailsService = doctorDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(Authentication authentication){
        if (authentication != null && authentication.isAuthenticated()){
            //Perform the token generation procedure and return the token as a response
            LoginResponse response = this.authService.login(authentication);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body("Error");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AddPatientRequest addPatientRequest) throws AlreadyRegisteredUserException {
        RegisterResponse response = this.authService.register(addPatientRequest);
        return response.isSuccess() ?
                ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @PostMapping("/admin/doctor/create")
    public ResponseEntity<?> addDoctor(@RequestBody AddDoctorRequest addDoctorRequest, Authentication authentication) throws AlreadyRegisteredUserException {
        if (authentication != null){
            AddDoctorResponse response = this.authService.addDoctor(addDoctorRequest);
            return response.isSuccess() ?
                    ResponseEntity.ok(response)
                    :ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(response);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    @PatchMapping("/admin/doctor/edit/{doctorId}")
    public ResponseEntity<?> editDoctor(@RequestBody EditDoctorRequest editDoctorRequest, @PathVariable("doctorId") Long doctorId){
        Doctor updatedDoctor = (Doctor) doctorDetailsService.editUser(doctorId,editDoctorRequest);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "The doctor "+ updatedDoctor.getDoctorName() + " has been modified");

        return ResponseEntity.ok()
                .body(jsonObject.toString(jsonObject.length()));
    }

    @DeleteMapping("/admin/doctor/delete/{doctorId}")
    public ResponseEntity<?> deleteDoctor(@PathVariable("doctorId") Long doctorId){
        Doctor deletedDoctor = (Doctor) doctorDetailsService.removeUser(doctorId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "The doctor "+deletedDoctor.getDoctorName() + " has been removed");
        return ResponseEntity.ok()
                .body(jsonObject.toString(jsonObject.length()));
    }

    @PatchMapping("/patient/edit{patientId}")
    public ResponseEntity<?> editPatient(@PathVariable("patientId") Long patientId, @RequestBody EditPatientRequest request){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "the patient "+ authService.editPatient(patientId,request).getPatientName());
        return ResponseEntity.ok()
                .body(jsonObject.toString(jsonObject.length()));
    }

    @DeleteMapping("/patient/delete/{patientId}")
    public ResponseEntity<?> removePatient(@PathVariable("patientId") Long patientId){
        Patient patient = authService.removePatient(patientId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "Your profile has been successfully deleted");
        return ResponseEntity.ok()
                .body(jsonObject.toString(jsonObject.length()));
    }

}
