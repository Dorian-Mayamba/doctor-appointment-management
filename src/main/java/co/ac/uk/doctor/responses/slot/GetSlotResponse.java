package co.ac.uk.doctor.responses.slot;

import co.ac.uk.doctor.serializers.SlotSerializer;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetSlotResponse {
    List<SlotSerializer> slots;
}
