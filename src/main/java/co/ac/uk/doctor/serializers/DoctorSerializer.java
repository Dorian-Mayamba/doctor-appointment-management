package co.ac.uk.doctor.serializers;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DoctorSerializer {
    private String doctorName;

    private String doctorEmail;

    private Long doctorId;

    private String doctorSpeciality;

    private String doctorNumber;

    private String doctorProfile;

    private List<AppointmentSerializer> appointments;

    private List<ReviewSerializer> reviews;

    private List<RatingSerializer> ratings;

    private Double averageRating;
}
