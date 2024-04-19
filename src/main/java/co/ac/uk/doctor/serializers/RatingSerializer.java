package co.ac.uk.doctor.serializers;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RatingSerializer {
    private Double rating;
    private String patientName;
    private String patientPicture;
}
