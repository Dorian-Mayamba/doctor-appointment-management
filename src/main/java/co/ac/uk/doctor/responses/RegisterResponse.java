package co.ac.uk.doctor.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterResponse {
    private String message;
    private boolean isSuccess;
}
