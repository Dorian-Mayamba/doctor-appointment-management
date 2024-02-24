package co.ac.uk.doctor.exceptions;

import javax.naming.AuthenticationException;

public class AlreadyRegisteredUserException extends AuthenticationException {
    public AlreadyRegisteredUserException(String message){
        super(message);
    }
}
