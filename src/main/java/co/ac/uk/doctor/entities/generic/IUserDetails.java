package co.ac.uk.doctor.entities.generic;

import co.ac.uk.doctor.entities.Role;
import org.springframework.security.core.userdetails.UserDetails;

public interface IUserDetails extends UserDetails {
    Long getId();

    String getName();
    void setName(String name);

    String getEmail();

    void setEmail(String email);

    default void setProfile(String imagePath){
        throw new UnsupportedOperationException("Please implement the setProfile(String imagePath) method");
    }
    default String getProfile(){
        throw new UnsupportedOperationException("Please implement this method");
    }

    default void setNumber(String number){
        throw new UnsupportedOperationException("Please implement this method "+ getClass().toString());
    }
    default String getNumber(){
        throw new UnsupportedOperationException("Please implement the getNumber() method");
    }


    Role getRole();

}
