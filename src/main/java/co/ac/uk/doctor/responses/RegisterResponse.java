package co.ac.uk.doctor.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterResponse {
    private String message;
    private boolean isSuccess;
}
