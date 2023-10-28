package co.ac.uk.doctor.providers;

import co.ac.uk.doctor.generic.IUserDetails;
import co.ac.uk.doctor.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class DoctorAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(!this.supports(authentication.getClass())){
            return null;
        }
        IUserDetails userDetails = (IUserDetails) authentication.getPrincipal();
        return UsernamePasswordAuthenticationToken.authenticated(
                userDetails.getUsername(),userDetails.getPassword(),userDetails.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
