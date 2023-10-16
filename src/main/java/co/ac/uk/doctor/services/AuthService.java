package co.ac.uk.doctor.services;

import co.ac.uk.doctor.entities.Role;
import co.ac.uk.doctor.entities.User;
import co.ac.uk.doctor.requests.RegisterRequest;
import co.ac.uk.doctor.responses.LoginResponse;
import co.ac.uk.doctor.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AuthService {
    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    private final RoleService roleService;
    @Autowired
    public AuthService(UserService userService, JWTUtil jwtUtil, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, RoleService roleService){
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.roleService = roleService;
    }

    public LoginResponse login(String userName, String password) throws UsernameNotFoundException, BadCredentialsException {
        try{
            LoginResponse response = new LoginResponse();
            User user = (User) this.userDetailsService.loadUserByUsername(userName);
            if(!passwordEncoder.matches(password, user.getPassword())){
                throw new BadCredentialsException("Incorrect username or password");
            }
            response.setId(user.getId());
            response.setCurrentUserName(user.getName());
            response.setAccessToken(jwtUtil.generateToken(user));
            return response;
        }catch (UsernameNotFoundException ex){
            throw ex;
        }
    }

    public ResponseEntity<?> register(RegisterRequest registerRequest){
        Optional<User> userToFind = userService.findByEmail(registerRequest.getEmail());
        if(userToFind.isPresent()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The user "+ registerRequest.getName() + " is already saved in our records");
        }
        Role role = getRoleByType(registerRequest.getRoleType());
        User user = userService.save( new User(registerRequest.getName(), registerRequest.getEmail(), passwordEncoder.encode(registerRequest.getPassword()),role));
        role.addUser(user);
        return ResponseEntity.ok("The user "+ user.getName() + " has been saved in our records");
    }

    private Role getRoleByType(String type){
        return this.roleService.findByType(type);
    }
}
