package co.ac.uk.doctor.providers;

import co.ac.uk.doctor.constants.RoleConstants;
import co.ac.uk.doctor.entities.generic.IUserDetails;
import co.ac.uk.doctor.services.AdminDetailsService;
import co.ac.uk.doctor.services.DoctorService;
import co.ac.uk.doctor.services.PatientService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DoctorAuthenticationProvider implements AuthenticationProvider {

    private DoctorService doctorService;

    private PatientService patientService;

    private AdminDetailsService adminDetailsService;

    private PasswordEncoder passwordEncoder;
    public DoctorAuthenticationProvider(){}

    public DoctorService getDoctorDetailsService() {
        return doctorService;
    }

    public void setDoctorDetailsService(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    public PatientService getPatientDetailsService() {
        return patientService;
    }

    public void setPatientDetailsService(PatientService patientService) {
        this.patientService = patientService;
    }

    public AdminDetailsService getAdminDetailsService() {
        return adminDetailsService;
    }

    public void setAdminDetailsService(AdminDetailsService adminDetailsService) {
        this.adminDetailsService = adminDetailsService;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(!this.supports(authentication.getClass())){
            return null;
        }
        if (authentication.isAuthenticated()){
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
            IUserDetails userDetails = (IUserDetails) token.getPrincipal();
            token.setDetails(userDetails);
            return token;
        }
        IUserDetails userDetails = null;
        String role = getRole(authentication.getName());
        switch (role){
            case RoleConstants.DOCTOR :
                userDetails = (IUserDetails) this.doctorService.loadUserByUsername(authentication.getName());
                break;
            case RoleConstants.ADMIN:
                userDetails = (IUserDetails) this.adminDetailsService.loadUserByUsername(authentication.getName());
                break;
            case RoleConstants.PATIENT:
                userDetails = (IUserDetails) this.patientService.loadUserByUsername(authentication.getName());
                break;
        }
        if (userDetails != null){
            if (!passwordEncoder.matches(authentication.getCredentials().toString(), userDetails.getPassword())){
                return null;
            }else{
                UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.authenticated(userDetails, null,userDetails.getAuthorities());
                token.setDetails(userDetails);
                return token;
            }
        }
        return null;
    }


    protected String getRole(String username){
        if (username.contains("@admin")){
            return RoleConstants.ADMIN;
        }else if(username.contains("@doctor")){
            return RoleConstants.DOCTOR;
        }else{
            return RoleConstants.PATIENT;
        }
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
