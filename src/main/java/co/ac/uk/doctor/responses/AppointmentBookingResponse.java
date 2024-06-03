package co.ac.uk.doctor.responses;

import co.ac.uk.doctor.serializers.AppointmentSerializer;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AppointmentBookingResponse {
    private String message;

    private AppointmentSerializer appointment;
}
