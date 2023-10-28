package co.ac.uk.doctor.filters;

import co.ac.uk.doctor.claims.DoctorRegisteredClaims;
import co.ac.uk.doctor.entities.Admin;
import co.ac.uk.doctor.entities.Doctor;
import co.ac.uk.doctor.entities.Patient;
import co.ac.uk.doctor.generic.IUserDetailsService;
import co.ac.uk.doctor.utils.JWTUtil;
import co.ac.uk.doctor.constants.RoleConstants;
import com.auth0.jwt.RegisteredClaims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class JWTFilter extends OncePerRequestFilter {
    private final String TOKEN_KEY = "jwt_token";
    private final JWTUtil jwtUtil;
    @Autowired
    public JWTFilter(JWTUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Filter initialized");
        filterChain.doFilter(request,response);
    }

    private String extractTokenFromRequest(HttpServletRequest request){
        List<Cookie> cookies =Arrays.asList(request.getCookies());
        Cookie cookie = this.getCookieByKey(cookies,this.TOKEN_KEY);
        if(cookie != null){
            return cookie.getValue();
        }
        return null;
    }

    private Cookie getCookieByKey(List<Cookie> cookies, String token_key){
        for(Cookie cookie:cookies){
            if(cookie.getName().equals(token_key)){
                return cookie;
            }
        }
        return null;
    }


}
