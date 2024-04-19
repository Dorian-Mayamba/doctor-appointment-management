package co.ac.uk.doctor.serializers;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReviewSerializer {
    private String content;
    private String patientName;
    private String patientPicture;
}
