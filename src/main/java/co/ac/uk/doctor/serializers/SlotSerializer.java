package co.ac.uk.doctor.serializers;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SlotSerializer {
    private Long id;
    private LocalDate date;
    private LocalTime time;
    private Month month;
    private boolean isBooked;
}
