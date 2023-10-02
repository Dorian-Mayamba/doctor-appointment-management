package co.ac.uk.doctor.responses;


import lombok.Data;
@Data
public class LoginResponse {
    private String currentUserName;
    private String accessToken;
    private Long id;
}
