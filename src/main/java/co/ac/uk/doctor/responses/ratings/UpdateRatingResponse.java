package co.ac.uk.doctor.responses.ratings;

import co.ac.uk.doctor.serializers.RatingSerializer;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdateRatingResponse {
    private String message;
    private RatingSerializer ratingData;
}
