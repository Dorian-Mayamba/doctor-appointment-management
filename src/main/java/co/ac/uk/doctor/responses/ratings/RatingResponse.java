package co.ac.uk.doctor.responses.ratings;

import co.ac.uk.doctor.serializers.RatingSerializer;
import co.ac.uk.doctor.serializers.ReviewSerializer;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RatingResponse {
    private List<RatingSerializer> ratings;
    private Double averageRating;
}
