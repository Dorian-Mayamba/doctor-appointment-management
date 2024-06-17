package co.ac.uk.doctor.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "doctor_appointments_view")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorAppointmentsView{

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "patients", column = @Column(name = "patients")),
            @AttributeOverride(name = "doctorName", column = @Column(name = "doctor_name")),
            @AttributeOverride(name = "status", column = @Column(name = "status")),
            @AttributeOverride(name = "doctorProfile", column = @Column(name = "doctor_profile")),
            @AttributeOverride(name = "date", column = @Column(name = "date")),
            @AttributeOverride(name = "time", column = @Column(name = "time")),
            @AttributeOverride(name = "service", column = @Column(name = "service"))
    })
    AppointmentComposite appointmentComposite;
}
