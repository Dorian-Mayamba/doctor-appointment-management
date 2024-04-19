package co.ac.uk.doctor.services;

import co.ac.uk.doctor.entities.Doctor;
import co.ac.uk.doctor.entities.Patient;
import co.ac.uk.doctor.exceptions.AlreadyRegisteredUserException;
import co.ac.uk.doctor.entities.generic.IUserDetails;
import co.ac.uk.doctor.services.generic.IUserDetailsService;
import co.ac.uk.doctor.requests.AddDoctorRequest;
import co.ac.uk.doctor.requests.AddPatientRequest;
import co.ac.uk.doctor.requests.EditPatientRequest;
import co.ac.uk.doctor.responses.AddDoctorResponse;
import co.ac.uk.doctor.responses.LoginResponse;
import co.ac.uk.doctor.responses.RegisterResponse;
import co.ac.uk.doctor.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    private final IUserDetailsService adminDetailsService;

    private final IUserDetailsService patientDetailsService;

    private final IUserDetailsService doctorDetailsService;
    private final RoleService roleService;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(JWTUtil jwtUtil, PasswordEncoder passwordEncoder, @Qualifier("createAdminDetailsService") IUserDetailsService adminDetailsService, @Qualifier("createPatientDetailsService") IUserDetailsService<Patient> patientDetailsService,
                       @Qualifier("createDoctorDetailsService") IUserDetailsService<Doctor> doctorDetailsService, RoleService roleService, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.adminDetailsService = adminDetailsService;
        this.patientDetailsService = patientDetailsService;
        this.doctorDetailsService = doctorDetailsService;
        this.roleService = roleService;
        this.authenticationManager = authenticationManager;
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
        response.setRoleType(userDetails.getRole().getType());
        return response;
    }

    public RegisterResponse register(AddPatientRequest addPatientRequest) throws AlreadyRegisteredUserException {
        RegisterResponse response = new RegisterResponse();
        PatientService patientService = (PatientService) patientDetailsService;
        Patient patient = patientService.addUser(addPatientRequest);
        response.setSuccess(true);
        response.setMessage("Your account has successfully been registered");
        return response;
    }

    public AddDoctorResponse addDoctor(AddDoctorRequest addDoctorRequest) throws AlreadyRegisteredUserException {
        AddDoctorResponse response = new AddDoctorResponse();
        DoctorService doctorService = (DoctorService) doctorDetailsService;
        Doctor doctor = doctorService.addUser(addDoctorRequest);
        response.setSuccess(true);
        response.setMessage("The doctor "+ doctor.getDoctorName() + " has been added");
        return response;
    }

    public Patient editPatient(Long patientId, EditPatientRequest editPatientRequest) {
        Patient editedPatient = (Patient) patientDetailsService.editUser(patientId, editPatientRequest);
        return editedPatient;
    }

    public Patient removePatient(Long patientId) {
        return (Patient) patientDetailsService.removeUser(patientId);
    }
}
