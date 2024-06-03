package co.ac.uk.doctor.serializers;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReviewSerializer {
    private String content;
    private Double rating;
    private Double avgRating;
    private String patientName;
    private String patientPicture;
}
