package co.ac.uk.doctor.responses;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class LoginResponse {
    private String currentUserName;
    private String accessToken;
    private Long id;
    private String roleType;
}
