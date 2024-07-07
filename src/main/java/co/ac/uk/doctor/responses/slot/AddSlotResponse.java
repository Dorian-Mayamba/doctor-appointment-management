package co.ac.uk.doctor.responses.slot;

import co.ac.uk.doctor.serializers.SlotSerializer;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddSlotResponse {
    private String message;
    private SlotSerializer slot;
}
