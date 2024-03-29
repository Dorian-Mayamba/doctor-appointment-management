package co.ac.uk.doctor.serializers;

import co.ac.uk.doctor.entities.jpa.Appointment;
import lombok.*;

import java.util.ArrayList;
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

    private List<AppointmentSerializer> appointments;
}
