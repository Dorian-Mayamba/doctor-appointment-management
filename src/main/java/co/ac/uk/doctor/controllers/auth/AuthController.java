package co.ac.uk.doctor.controllers.auth;

import co.ac.uk.doctor.constants.ApplicationConstants;
import co.ac.uk.doctor.exceptions.AlreadyRegisteredUserException;
import co.ac.uk.doctor.requests.LoginRequest;
import co.ac.uk.doctor.requests.RegisterRequest;
import co.ac.uk.doctor.responses.LoginResponse;
import co.ac.uk.doctor.responses.RegisterResponse;
import co.ac.uk.doctor.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;

    private final SecurityContextRepository repository;
    @Autowired
    public AuthController(AuthService authService, SecurityContextRepository repository){
        this.authService = authService;
        this.repository = repository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response){
        try{
            LoginResponse loginResponse = this.authService.login(loginRequest.getEmail(),loginRequest.getPassword(),loginRequest.getType());
            Cookie cookie = new Cookie(ApplicationConstants.JWT_TOKEN, loginResponse.getAccessToken());
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            SecurityContext ctx = SecurityContextHolder.getContext();
            this.repository.saveContext(ctx,request,response);
            log.info("Login method fired");
            return ResponseEntity.ok(loginResponse);
        }catch (UsernameNotFoundException | BadCredentialsException ex){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ex.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) throws AlreadyRegisteredUserException {
        RegisterResponse response = this.authService.register(registerRequest);
        return response.isSuccess() ?
                ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }
}
