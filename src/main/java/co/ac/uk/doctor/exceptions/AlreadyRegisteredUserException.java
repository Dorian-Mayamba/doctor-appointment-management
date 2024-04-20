package co.ac.uk.doctor.exceptions;


import org.springframework.security.core.AuthenticationException;

public class AlreadyRegisteredUserException extends AuthenticationException {
    public AlreadyRegisteredUserException(String message){
        super(message);
    }
}
