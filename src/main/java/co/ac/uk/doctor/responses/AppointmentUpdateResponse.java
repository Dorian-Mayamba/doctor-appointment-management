package co.ac.uk.doctor.responses;

import co.ac.uk.doctor.serializers.AppointmentSerializer;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AppointmentUpdateResponse {
    private String message;

    private AppointmentSerializer appointmentSerializer;
}
