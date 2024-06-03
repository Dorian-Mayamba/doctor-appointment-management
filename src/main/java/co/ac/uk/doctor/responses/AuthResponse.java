package co.ac.uk.doctor.responses;

import co.ac.uk.doctor.serializers.AppointmentSerializer;
import co.ac.uk.doctor.serializers.RatingSerializer;
import co.ac.uk.doctor.serializers.ReviewSerializer;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AuthResponse {
    private String currentUserName;
    private String accessToken;
    private String email;
    private String userProfile;
    private Long id;
    private String roleType;
    private String number;
    private boolean isSuccess;
    private String message;
    private List<AppointmentSerializer> appointments;
    List<RatingSerializer> ratings;
    List<ReviewSerializer> reviews;
}
