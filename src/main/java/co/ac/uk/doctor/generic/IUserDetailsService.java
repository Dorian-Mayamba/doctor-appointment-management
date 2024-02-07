package co.ac.uk.doctor.generic;

import co.ac.uk.doctor.exceptions.AlreadyRegisteredUserException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUserDetailsService extends UserDetailsService {
    UserDetails loadUserById(Long id);

    void checkUserInDatabase(String username) throws AlreadyRegisteredUserException;

    List<IUserDetails> getUsers();
}
