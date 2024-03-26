package co.ac.uk.doctor.serializers;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PatientSerializer {
    private String patientName;

    private String patientEmail;

    private String patientNumber;

    private Long patientId;

    private List<AppointmentSerializer> appointmentSerializerList;
}
