package co.ac.uk.doctor.providers;

import co.ac.uk.doctor.constants.RoleConstants;
import co.ac.uk.doctor.entities.generic.IUserDetails;
import co.ac.uk.doctor.services.AdminDetailsService;
import co.ac.uk.doctor.services.DoctorDetailsService;
import co.ac.uk.doctor.services.PatientDetailsService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DoctorAuthenticationProvider implements AuthenticationProvider {

    private DoctorDetailsService doctorDetailsService;

    private PatientDetailsService patientDetailsService;

    private AdminDetailsService adminDetailsService;

    private PasswordEncoder passwordEncoder;
    public DoctorAuthenticationProvider(){}

    public DoctorDetailsService getDoctorDetailsService() {
        return doctorDetailsService;
    }

    public void setDoctorDetailsService(DoctorDetailsService doctorDetailsService) {
        this.doctorDetailsService = doctorDetailsService;
    }

    public PatientDetailsService getPatientDetailsService() {
        return patientDetailsService;
    }

    public void setPatientDetailsService(PatientDetailsService patientDetailsService) {
        this.patientDetailsService = patientDetailsService;
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
        IUserDetails userDetails = null;
        String role = getRole(authentication.getName());
        switch (role){
            case RoleConstants.DOCTOR :
                userDetails = (IUserDetails) this.doctorDetailsService.loadUserByUsername(authentication.getName());
                break;
            case RoleConstants.ADMIN:
                userDetails = (IUserDetails) this.adminDetailsService.loadUserByUsername(authentication.getName());
                break;
            case RoleConstants.PATIENT:
                userDetails = (IUserDetails) this.patientDetailsService.loadUserByUsername(authentication.getName());
                break;
        }
        if (userDetails != null){
            if (!passwordEncoder.matches(authentication.getCredentials().toString(), userDetails.getPassword())){
                return null;
            }else{
                UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.authenticated(userDetails.getUsername(), userDetails.getPassword(),userDetails.getAuthorities());
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
