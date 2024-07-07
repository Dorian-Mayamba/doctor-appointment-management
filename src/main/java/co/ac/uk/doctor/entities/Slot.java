package co.ac.uk.doctor.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @Column(name = "month", nullable = false)
    private Month month;

    @Column(nullable = false)
    private boolean isBooked = false;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
}
