package co.ac.uk.doctor.services;

import co.ac.uk.doctor.entities.Admin;
import co.ac.uk.doctor.entities.Doctor;
import co.ac.uk.doctor.entities.Role;
import co.ac.uk.doctor.entities.Patient;
import co.ac.uk.doctor.exceptions.AlreadyRegisteredUserException;
import co.ac.uk.doctor.generic.IUserDetails;
import co.ac.uk.doctor.generic.IUserDetailsService;
import co.ac.uk.doctor.requests.RegisterRequest;
import co.ac.uk.doctor.responses.LoginResponse;
import co.ac.uk.doctor.responses.RegisterResponse;
import co.ac.uk.doctor.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import co.ac.uk.doctor.constants.RoleConstants;

import java.util.Map;

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
    public AuthService(JWTUtil jwtUtil, PasswordEncoder passwordEncoder, @Qualifier("createAdminDetailsService") IUserDetailsService adminDetailsService, @Qualifier("createPatientDetailsService") IUserDetailsService patientDetailsService,
                       @Qualifier("createDoctorDetailsService") IUserDetailsService doctorDetailsService, RoleService roleService, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.adminDetailsService = adminDetailsService;
        this.patientDetailsService = patientDetailsService;
        this.doctorDetailsService = doctorDetailsService;
        this.roleService = roleService;
        this.authenticationManager = authenticationManager;
    }

    public LoginResponse login(Authentication authentication) throws UsernameNotFoundException, BadCredentialsException {
        LoginResponse response = new LoginResponse();
        IUserDetails userDetails = (IUserDetails) authentication.getDetails();
        response.setCurrentUserName(authentication.getName());
        response.setId(userDetails.getId());
        response.setAccessToken(jwtUtil.generateToken(authentication));
        response.setRoleType(userDetails.getRole().getType());
        return response;
    }

    public RegisterResponse register(RegisterRequest registerRequest) throws AlreadyRegisteredUserException {
        RegisterResponse response = new RegisterResponse();
        String roleName = this.findRoleNameByEmail(registerRequest.getEmail());
        Map<String, Long> roles = RoleConstants.getRoles();
        switch (roleName) {
            case RoleConstants.PATIENT:
                try {
                    Long roleId = roles.get(roleName);
                    Role role = this.roleService.findRoleById(roleId);
                    Patient patient = new Patient(registerRequest.getName(), registerRequest.getEmail(),
                            passwordEncoder.encode(registerRequest.getPassword()), role, registerRequest.getNumber());
                    checkUserInDatabase(patient);
                    Patient savedPatient = ((PatientDetailsService) this.patientDetailsService).savePatient(patient);
                    response.setMessage("The user " + savedPatient.getUsername() + " has been saved in our records");
                    response.setSuccess(true);
                } catch (AlreadyRegisteredUserException exception) {
                    response.setMessage(exception.getMessage());
                    response.setSuccess(false);
                }
        }
        return response;
    }

    private void checkUserInDatabase(UserDetails user) throws AlreadyRegisteredUserException {
        if (user instanceof Admin) {
            try {
                Admin a = (Admin) this.adminDetailsService.loadUserByUsername(((Admin) user).getAdminEmail());
                if (a != null) {
                    throw new AlreadyRegisteredUserException("This account is already saved in our records");
                }
            } catch (UsernameNotFoundException ex) {
                return;
            }
        } else if (user instanceof Patient) {
            try {
                Patient p = (Patient) this.patientDetailsService.loadUserByUsername(((Patient) user).getPatientEmail());
                if (p != null) {
                    throw new AlreadyRegisteredUserException("This account is already saved in our records");
                }
            } catch (UsernameNotFoundException ex) {
                return;
            }
        } else if (user instanceof Doctor) {
            try {
                Doctor d = (Doctor) this.doctorDetailsService.loadUserByUsername(((Doctor) user).getDoctorEmail());
                if (d != null) {
                    throw new AlreadyRegisteredUserException("This account is already saved in our records");
                }
            } catch (UsernameNotFoundException ex) {
                return;
            }
        }
    }

    private Role getRoleById(Long id) {
        return this.roleService.findRoleById(id);
    }

    private String findRoleNameByEmail(String email) {
        if (email.contains("@admin.ac.uk")) {
            return RoleConstants.ADMIN;
        } else if (email.contains("@doctor.ac.uk")) {
            return RoleConstants.DOCTOR;
        } else {
            return RoleConstants.PATIENT;
        }
    }
}
