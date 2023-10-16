package co.ac.uk.doctor.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String name;

    private String email;

    private String password;

    private String roleType;
}
