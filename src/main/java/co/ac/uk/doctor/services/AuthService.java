package co.ac.uk.doctor.services;

import co.ac.uk.doctor.entities.Patient;
import co.ac.uk.doctor.exceptions.AlreadyRegisteredUserException;
import co.ac.uk.doctor.entities.generic.IUserDetails;
import co.ac.uk.doctor.requests.AddPatientRequest;
import co.ac.uk.doctor.responses.LoginResponse;
import co.ac.uk.doctor.responses.RegisterResponse;
import co.ac.uk.doctor.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final JWTUtil jwtUtil;

    private final PatientService patientService;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(JWTUtil jwtUtil,PatientService patientService, AuthenticationManager manager) {
        this.jwtUtil = jwtUtil;
        this.patientService = patientService;
        this.authenticationManager = manager;
    }

    public LoginResponse login(Authentication authentication) throws UsernameNotFoundException, BadCredentialsException {
        LoginResponse response = LoginResponse
                .builder().
                build();
        IUserDetails userDetails = (IUserDetails) authentication.getDetails();
        response.setCurrentUserName(userDetails.getName());
        response.setId(userDetails.getId());
        response.setAccessToken(jwtUtil.generateToken(authentication));
        response.setEmail(userDetails.getUsername());
        response.setNumber(userDetails.getNumber());
        response.setUserProfile(userDetails.getProfile());
        response.setRoleType(userDetails.getRole().getType());
        return response;
    }

    public RegisterResponse register(AddPatientRequest addPatientRequest) throws AlreadyRegisteredUserException {
        RegisterResponse response = new RegisterResponse();
        Patient patient = this.patientService.addUser(addPatientRequest);
        IUserDetails userDetails = (IUserDetails) patientService.loadUserByUsername(patient.getEmail());
        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.authenticated(userDetails, null, userDetails.getAuthorities());
        Authentication auth = this.authenticationManager.authenticate(authenticationToken);
        response.setCurrentUserName(userDetails.getName());
        response.setId(userDetails.getId());
        response.setAccessToken(jwtUtil.generateToken(auth));
        response.setEmail(userDetails.getUsername());
        response.setNumber(userDetails.getNumber());
        response.setUserProfile(userDetails.getProfile());
        response.setRoleType(userDetails.getRole().getType());
        response.setSuccess(true);
        response.setMessage("Your account has been created, you are now logged in");
        return response;
    }
}
