package co.ac.uk.doctor.exceptions;

public class AlreadyRegisteredUserException extends Exception{
    public AlreadyRegisteredUserException(String message){
        super(message);
    }
}
