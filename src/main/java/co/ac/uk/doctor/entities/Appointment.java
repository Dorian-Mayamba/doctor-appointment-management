package co.ac.uk.doctor.entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private Time time;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Appointment(Date date, Time time, User user){
        this.user = user;
        this.date = date;
        this.time = time;
    }

    public Appointment(){

    }

    public User getUser() {
        return user;
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
