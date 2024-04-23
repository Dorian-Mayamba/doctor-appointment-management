package co.ac.uk.doctor.controllers.auth;
import co.ac.uk.doctor.exceptions.AlreadyRegisteredUserException;
import co.ac.uk.doctor.requests.*;
import co.ac.uk.doctor.responses.LoginResponse;
import co.ac.uk.doctor.responses.RegisterResponse;
import co.ac.uk.doctor.services.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
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
}
