package co.ac.uk.doctor.services;

import co.ac.uk.doctor.entities.User;
import co.ac.uk.doctor.requests.RegisterRequest;
import co.ac.uk.doctor.responses.LoginResponse;
import co.ac.uk.doctor.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AuthService {
    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    public AuthService(UserService userService, JWTUtil jwtUtil, BCryptPasswordEncoder passwordEncoder){
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(String userName, String password) throws UsernameNotFoundException, BadCredentialsException {
        LoginResponse response = new LoginResponse();
        User user;
        Optional<User> userToFind = Optional.ofNullable(this.userService.findByEmail(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName + " not found in our records")));
        if(userToFind.isPresent()){
            user = userToFind.get();
            if(passwordEncoder.matches(password, user.getPassword())){
                response.setCurrentUserName(userName);
                response.setAccessToken(this.jwtUtil.generateToken(user));
                response.setId(user.getId());
                return response;
            }else{
                throw new BadCredentialsException("Incorrect email or password");
            }
        }
        return null;
    }

    public ResponseEntity<?> register(RegisterRequest registerRequest){
        Optional<User> userToFind = userService.findByEmail(registerRequest.getEmail());
        if(userToFind.isPresent()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The user "+ registerRequest.getName() + " is already saved in our records");
        }
        User user = userService.save( new User(registerRequest.getName(), registerRequest.getEmail(), passwordEncoder.encode(registerRequest.getPassword())));
        return ResponseEntity.ok("The user "+ user.getName() + " has been saved in our records");
    }
}
