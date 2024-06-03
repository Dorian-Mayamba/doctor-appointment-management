package co.ac.uk.doctor.responses.reviews;

import co.ac.uk.doctor.serializers.ReviewSerializer;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReviewResponse {
    private String message;
    private ReviewSerializer review;
}
