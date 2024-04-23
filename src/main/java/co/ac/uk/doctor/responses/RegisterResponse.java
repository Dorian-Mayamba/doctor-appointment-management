package co.ac.uk.doctor.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterResponse {
    private String message;
    private String currentUserName;
    private Long id;
    private String accessToken;
    private String email;
    private String number;
    private String userProfile;
    private String roleType;
    private boolean isSuccess;
}
