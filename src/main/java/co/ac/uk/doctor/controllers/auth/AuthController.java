package co.ac.uk.doctor.controllers.auth;

import co.ac.uk.doctor.constants.ApplicationConstants;
import co.ac.uk.doctor.requests.LoginRequest;
import co.ac.uk.doctor.requests.RegisterRequest;
import co.ac.uk.doctor.responses.LoginResponse;
import co.ac.uk.doctor.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository
            = new HttpSessionSecurityContextRepository();
    private final SecurityContextHolderStrategy strategy;
    @Autowired
    public AuthController(AuthService authService, AuthenticationManager authenticationManager, SecurityContextHolderStrategy strategy){
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.strategy = strategy;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response){
        try{
            LoginResponse res = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
            UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(
                    res.getCurrentUserName(), loginRequest.getPassword()
            );
            Authentication auth = authenticationManager.authenticate(token);
            SecurityContext context = strategy.createEmptyContext();
            context.setAuthentication(auth);
            strategy.setContext(context);
            securityContextRepository.saveContext(context,request,response);
            Cookie cookie = new Cookie(ApplicationConstants.JWT_TOKEN, res.getAccessToken());
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            return ResponseEntity.ok(res);
        }catch(UsernameNotFoundException ex){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }catch (BadCredentialsException ex){
            return ResponseEntity.badRequest()
                    .body(ex.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest){
        try{
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(this.authService.register(registerRequest));
        }catch(ResponseStatusException ex){
            return ResponseEntity
                    .badRequest()
                    .body(ex.getMessage());
        }
    }
}
