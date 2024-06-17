package co.ac.uk.doctor.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentComposite implements Serializable {
    private int patients;
    private String doctorName;
    private Appointment.Status status;
    private String doctorProfile;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;
    private LocalDate date;
    private String service;
}
