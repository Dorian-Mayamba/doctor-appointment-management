package co.ac.uk.doctor.entities.generic;

import co.ac.uk.doctor.entities.Role;
import org.springframework.security.core.userdetails.UserDetails;

public interface IUserDetails extends UserDetails {
    Long getId();

    String getName();

    default void setUserProfile(String imagePath){
        throw new UnsupportedOperationException("Please implement the setProfile(String imagePath) method");
    }

    default String getUserProfile(){
        throw new UnsupportedOperationException("Please implement this method");
    }

    Role getRole();

    default String getNumber(){
        throw new UnsupportedOperationException("Please implement the getNumber() method");
    }
}
