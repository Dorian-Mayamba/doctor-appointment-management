package co.ac.uk.doctor.generic;

import co.ac.uk.doctor.entities.Role;
import org.springframework.security.core.userdetails.UserDetails;

public interface IUserDetails extends UserDetails {
    Long getId();

    Role getRole();
}
