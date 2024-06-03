package co.ac.uk.doctor.services;

import co.ac.uk.doctor.entities.Doctor;
import co.ac.uk.doctor.entities.Patient;
import co.ac.uk.doctor.exceptions.AlreadyRegisteredUserException;
import co.ac.uk.doctor.entities.generic.IUserDetails;
import co.ac.uk.doctor.requests.AddPatientRequest;
import co.ac.uk.doctor.responses.AuthResponse;
import co.ac.uk.doctor.responses.LoginResponse;
import co.ac.uk.doctor.responses.RegisterResponse;
import co.ac.uk.doctor.utils.EntityToSerializerConverter;
import co.ac.uk.doctor.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
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
        IUserDetails userDetails = (IUserDetails) authentication.getDetails();
        return (LoginResponse) buildResponse(userDetails,authentication, "Hello "+ userDetails.getName(), null);
    }

    public RegisterResponse register(AddPatientRequest addPatientRequest) throws AlreadyRegisteredUserException {
        Patient patient = this.patientService.addUser(addPatientRequest);
        IUserDetails userDetails = (IUserDetails) patientService.loadUserByUsername(patient.getEmail());
        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.authenticated(userDetails, null, userDetails.getAuthorities());
        Authentication auth = this.authenticationManager.authenticate(authenticationToken);
        return (RegisterResponse) buildResponse(userDetails,authenticationToken, "Your account has been created, you are now logged in", addPatientRequest);
    }

    private AuthResponse buildResponse(IUserDetails userDetails, Authentication authentication, String message, @Nullable AddPatientRequest patientRequest){
        AuthResponse response;
        if(patientRequest != null){
            response = new RegisterResponse();
        }else{
            response = new LoginResponse();
        }
        return getAuthResponse(userDetails, authentication, message, response);
    }

    private AuthResponse getAuthResponse(IUserDetails userDetails, Authentication authentication, String message, AuthResponse response) {
        response.setCurrentUserName(userDetails.getName());
        response.setId(userDetails.getId());
        response.setAccessToken(jwtUtil.generateToken(authentication));
        response.setEmail(userDetails.getUsername());
        response.setNumber(userDetails.getNumber());
        response.setUserProfile(userDetails.getProfile());
        response.setRoleType(userDetails.getRole().getType());
        response.setSuccess(true);
        response.setMessage(message);
        if (userDetails instanceof Doctor){
            Doctor doctor = (Doctor) userDetails;
            response.setAppointments(EntityToSerializerConverter.toAppointmentsSerializer(doctor.getDoctorAppointments()));
            response.setRatings(EntityToSerializerConverter.toRatingSerializer(doctor.getRatings()));
            response.setReviews(EntityToSerializerConverter.toReviewSerializer(doctor.getReviews()));
        }else if(userDetails instanceof Patient){
            Patient patient = (Patient)userDetails;
            response.setAppointments(EntityToSerializerConverter.toAppointmentsSerializer(patient.getPatientAppointments()));
            response.setRatings(EntityToSerializerConverter.toRatingSerializer(patient.getRatings()));
            response.setReviews(EntityToSerializerConverter.toReviewSerializer(patient.getReviews()));
        }
        return response;
    }
}
