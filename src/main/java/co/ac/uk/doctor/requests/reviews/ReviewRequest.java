package co.ac.uk.doctor.requests.reviews;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReviewRequest {
    private String content;
}
