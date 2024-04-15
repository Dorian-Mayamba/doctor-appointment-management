package co.ac.uk.doctor.userdetails.generic;

import co.ac.uk.doctor.userdetails.Role;
import org.springframework.security.core.userdetails.UserDetails;

public interface IUserDetails extends UserDetails {
    Long getId();

    String getName();

    default void setUserProfile(String imagePath){
        throw new UnsupportedOperationException("Please implement this method");
    }

    default String getUserProfile(){
        throw new UnsupportedOperationException("Please implement this method");
    }

    Role getRole();
}
