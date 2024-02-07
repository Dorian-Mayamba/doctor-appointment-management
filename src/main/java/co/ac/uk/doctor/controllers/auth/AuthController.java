package co.ac.uk.doctor.controllers.auth;
import co.ac.uk.doctor.entities.Doctor;
import co.ac.uk.doctor.exceptions.AlreadyRegisteredUserException;
import co.ac.uk.doctor.generic.IUserDetailsService;
import co.ac.uk.doctor.requests.AddDoctorRequest;
import co.ac.uk.doctor.requests.EditDoctorRequest;
import co.ac.uk.doctor.requests.RegisterRequest;
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
import org.springframework.security.core.userdetails.UserDetailsService;
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
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) throws AlreadyRegisteredUserException {
        RegisterResponse response = this.authService.register(registerRequest);
        return response.isSuccess() ?
                ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @PostMapping("/admin/doctor/create")
    public ResponseEntity<?> addDoctor(@RequestBody AddDoctorRequest addDoctorRequest, Authentication authentication) {
        if (authentication !=null){
            log.info("Authenticated has "+ authentication.getName());
            Doctor doctor = new Doctor(addDoctorRequest.getDoctorName(),
                    addDoctorRequest.getDoctorEmail(),
                    roleService.findByType(RoleCheckerUtil.checkRoleByEmail(addDoctorRequest.getDoctorEmail())),
                    addDoctorRequest.getDoctorSpeciality());
            try{
                doctorDetailsService.checkUserInDatabase(doctor.getDoctorEmail());
                Doctor savedDoctor = ((DoctorDetailsService)this.doctorDetailsService).saveDoctor(doctor);
                assert savedDoctor != null : "Doctor could not be saved";
                AddDoctorResponse response = new AddDoctorResponse();
                response.setMessage("The doctor "+ doctor.getDoctorName() +" has been successfully saved");
                return ResponseEntity.ok()
                        .body(response.toString());
            }catch (AlreadyRegisteredUserException ex){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("error", ex.getMessage());
                return ResponseEntity
                        .badRequest()
                        .body(jsonObject.toString(jsonObject.length()));
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authenticated");
    }

    @PatchMapping("/admin/doctor/edit/{doctorId}")
    public ResponseEntity<?> editDoctor(@RequestBody EditDoctorRequest editDoctorRequest, @PathVariable("doctorId") Long doctorId){
        Doctor doctor = (Doctor) doctorDetailsService.loadUserById(doctorId);
        boolean edited = false;
        if (!(doctor.getDoctorEmail().equals(editDoctorRequest.getDoctorEmail()))){
            doctor.setDoctorEmail(editDoctorRequest.getDoctorEmail());
            edited = true;
        }

        if (!(doctor.getDoctorName().equals(editDoctorRequest.getDoctorName()))){
            doctor.setDoctorName(editDoctorRequest.getDoctorName());
            edited = true;
        }

        if (!(doctor.getSpeciality().equals(editDoctorRequest.getDoctorSpeciality()))){
            doctor.setSpeciality(editDoctorRequest.getDoctorSpeciality());
            edited = true;
        }

        Doctor updatedDoctor = ((DoctorDetailsService)doctorDetailsService).saveDoctor(doctor);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "The doctor "+ updatedDoctor.getDoctorName() + " has been modified");

        return ResponseEntity.ok()
                .body(jsonObject.toString(jsonObject.length()));
    }

}
