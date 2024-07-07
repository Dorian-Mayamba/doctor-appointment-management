package co.ac.uk.doctor.requests.slot;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddSlotRequest {
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;
    private Month month;
}
