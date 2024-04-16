package co.ac.uk.doctor.responses;

import co.ac.uk.doctor.requests.ProfileDTO;
import lombok.*;
import org.json.JSONObject;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponse {
    private String message;
    private String profile;
    String userData;
}
