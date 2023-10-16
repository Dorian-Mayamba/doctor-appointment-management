package co.ac.uk.doctor.responses;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class LoginResponse {
    private String currentUserName;
    private String accessToken;
    private Long id;
}
