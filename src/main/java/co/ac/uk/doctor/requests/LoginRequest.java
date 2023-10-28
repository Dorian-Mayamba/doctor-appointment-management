package co.ac.uk.doctor.requests;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;
    private String type;
}
