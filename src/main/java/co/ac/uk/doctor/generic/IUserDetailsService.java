package co.ac.uk.doctor.generic;

import co.ac.uk.doctor.entities.jpa.Role;
import co.ac.uk.doctor.exceptions.AlreadyRegisteredUserException;
import co.ac.uk.doctor.requests.AddUserRequest;
import co.ac.uk.doctor.requests.EditUserRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUserDetailsService<T> extends UserDetailsService {
    UserDetails loadUserById(Long id);

    void checkUserInDatabase(String username) throws AlreadyRegisteredUserException;

    List<T> getUsers();

    T removeUser(Long userId);

    T editUser(Long userId, EditUserRequest request);

    T addUser(AddUserRequest request) throws AlreadyRegisteredUserException;

    T saveUser(Exception exception, T user, AddUserRequest request);

    Role getRole(String roleName);
}
