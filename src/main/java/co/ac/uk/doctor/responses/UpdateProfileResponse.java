package co.ac.uk.doctor.responses;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdateProfileResponse {
    private String message;
    private String profile;
    String userData;
}
