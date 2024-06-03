package co.ac.uk.doctor.serializers;

import co.ac.uk.doctor.entities.Appointment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AppointmentSerializer {
    private String title;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    private LocalDate date;
    private Appointment.Status status;
    private String patientName, doctorName,patientPicture,doctorPicture,patientEmail,doctorEmail;
    private Long id;
}
