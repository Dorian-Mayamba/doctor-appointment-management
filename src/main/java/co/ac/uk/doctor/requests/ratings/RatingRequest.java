package co.ac.uk.doctor.requests.ratings;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RatingRequest {
    private Double rating;
}
