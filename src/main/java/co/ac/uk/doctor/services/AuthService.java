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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import co.ac.uk.doctor.constants.RoleConstants;

import javax.print.Doc;
import java.util.Optional;

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
    public AuthService(JWTUtil jwtUtil, PasswordEncoder passwordEncoder, @Qualifier("createAdminDetailsService") IUserDetailsService adminDetailsService,@Qualifier("createPatientDetailsService") IUserDetailsService patientDetailsService,
                       @Qualifier("createDoctorDetailsService") IUserDetailsService doctorDetailsService, RoleService roleService, AuthenticationManager authenticationManager){
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.adminDetailsService = adminDetailsService;
        this.patientDetailsService = patientDetailsService;
        this.doctorDetailsService = doctorDetailsService;
        this.roleService = roleService;
        this.authenticationManager = authenticationManager;
    }

    public LoginResponse login(String userName, String password, String type) throws UsernameNotFoundException, BadCredentialsException {
        LoginResponse response = new LoginResponse();
        switch (type) {
            case RoleConstants.ADMIN:
                try {
                    Admin admin = (Admin) this.adminDetailsService.loadUserByUsername(userName);
                        this.checkPasswords(password,admin.getPassword());
                        response.setId(admin.getId());
                        response.setAccessToken(this.jwtUtil.generateToken(admin));
                        response.setCurrentUserName(admin.getUsername());
                        this.authenticate(admin);
                } catch (UsernameNotFoundException | BadCredentialsException ex) {
                    throw ex;
                }
                break;
            case RoleConstants.DOCTOR:
                try {
                    Doctor doctor = (Doctor) this.doctorDetailsService.loadUserByUsername(userName);
                        this.checkPasswords(password,doctor.getPassword());
                        response.setId(doctor.getId());
                        response.setAccessToken(this.jwtUtil.generateToken(doctor));
                        response.setCurrentUserName(doctor.getUsername());
                        this.authenticate(doctor);
                } catch (UsernameNotFoundException | BadCredentialsException ex) {
                    throw ex;
                }
                break;
            case RoleConstants.PATIENT:
                try {
                    Patient patient = (Patient) this.patientDetailsService.loadUserByUsername(userName);
                        this.checkPasswords(password, patient.getPassword());
                        response.setId(patient.getId());
                        response.setAccessToken(this.jwtUtil.generateToken(patient));
                        response.setCurrentUserName(patient.getPatientName());
                        this.authenticate(patient);
                } catch (UsernameNotFoundException | BadCredentialsException ex) {
                    throw ex;
                }
                break;
        }
        return response;
    }

    public RegisterResponse register(RegisterRequest registerRequest)throws AlreadyRegisteredUserException {
        RegisterResponse response = new RegisterResponse();
        Role role = getRoleByType(registerRequest.getRoleType());
        switch (role.getType()){
            case RoleConstants.ADMIN:
                try{
                    Admin admin = new Admin(registerRequest.getName(), registerRequest.getEmail(),
                            passwordEncoder.encode(registerRequest.getPassword()),role);
                    checkUserInDatabase(admin);
                    AdminDetailsService service = (AdminDetailsService) adminDetailsService;
                    Admin savedAdmin = service.saveAdmin(admin);
                    response.setMessage("The user "+ savedAdmin.getUsername() + " has been saved in our records");
                    response.setSuccess(true);
                }catch (AlreadyRegisteredUserException exception){
                    response.setMessage(exception.getMessage());
                    response.setSuccess(false);
                }
                break;
            case RoleConstants.DOCTOR:
                try{
                    Doctor doctor = new Doctor(registerRequest.getName(), registerRequest.getEmail(), passwordEncoder.encode(registerRequest.getPassword()),role);
                    checkUserInDatabase(doctor);
                    Doctor savedDoctor = ((DoctorDetailsService)this.doctorDetailsService).saveDoctor(doctor);
                    response.setMessage("The user "+ savedDoctor.getUsername() + " has been saved in our records");
                    response.setSuccess(true);
                }catch (AlreadyRegisteredUserException exception){
                    response.setMessage(exception.getMessage());
                    response.setSuccess(false);
                }
            case RoleConstants.PATIENT:
                try{
                    Patient patient = new Patient(registerRequest.getName(),registerRequest.getEmail(),
                            passwordEncoder.encode(registerRequest.getPassword()),role);
                    checkUserInDatabase(patient);
                    Patient savedPatient = ((PatientDetailsService)this.patientDetailsService).savePatient(patient);
                    response.setMessage("The user "+ savedPatient.getUsername() + " has been saved in our records");
                    response.setSuccess(true);
                }catch (AlreadyRegisteredUserException exception){
                    response.setMessage(exception.getMessage());
                    response.setSuccess(false);
                }
        }
        return response;
    }

    private void checkUserInDatabase(UserDetails user) throws AlreadyRegisteredUserException{
        if(user instanceof Admin){
            try{
                Admin a = (Admin) this.adminDetailsService.loadUserByUsername(((Admin)user).getAdminEmail());
                if(a != null){
                    throw new AlreadyRegisteredUserException("This account is already saved in our records");
                }
            }catch(UsernameNotFoundException ex){
                return;
            }
        }else if( user instanceof Patient){
            try{
                Patient p = (Patient) this.patientDetailsService.loadUserByUsername(((Patient)user).getPatientEmail());
                if(p != null){
                    throw new AlreadyRegisteredUserException("This account is already saved in our records");
                }
            }catch(UsernameNotFoundException ex){
                return;
            }
        }else if(user instanceof Doctor){
            try{
                Doctor d = (Doctor) this.doctorDetailsService.loadUserByUsername(((Doctor)user).getDoctorEmail());
                if(d != null){
                    throw new AlreadyRegisteredUserException("This account is already saved in our records");
                }
            }catch(UsernameNotFoundException ex){
                return;
            }
        }
    }

    private Role getRoleByType(String roleType){
        return this.roleService.findByType(roleType);
    }

    protected void checkPasswords(CharSequence rawPassword,
                                  String encoredPassword) throws BadCredentialsException{
        if(!passwordEncoder.matches(rawPassword,encoredPassword)){
            throw new BadCredentialsException("incorrect email or password");
        }
    }

    public void authenticate(IUserDetails userDetails){
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(
                userDetails,null
        );
        Authentication auth = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
