package co.ac.uk.doctor.entities;

import co.ac.uk.doctor.generic.IUserDetails;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
@Entity
@Table(name = "doctors")
public class Doctor implements IUserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(nullable = false)
    private String doctorName;

    @Column(nullable = false, unique = true)
    private String doctorEmail;

    @Column
    private String doctorPassword;

    @Column(nullable = false)
    private String speciality;
    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public Doctor(String doctorName, String doctorEmail, String doctorPassword, Role role, String speciality){
        this.doctorName = doctorName;
        this.doctorEmail = doctorEmail;
        this.doctorPassword = doctorPassword;
        this.speciality = speciality;
        this.role = role;
    }

    public Doctor(String doctorName, String doctorEmail, Role role, String speciality){
        this.doctorName = doctorName;
        this.doctorEmail = doctorEmail;
        this.speciality = speciality;
        this.role = role;
    }

    @Override
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public String getDoctorPassword() {
        return doctorPassword;
    }

    public void setDoctorPassword(String doctorPassword) {
        this.doctorPassword = doctorPassword;
    }

    public void setDoctorAppointments(List<Appointment> doctorAppointments) {
        this.doctorAppointments = doctorAppointments;
    }

    public Doctor(){}

    @OneToMany(mappedBy = "doctor", fetch = FetchType.EAGER)
    List<Appointment> doctorAppointments;

    public List<Appointment> getDoctorAppointments() {
        return doctorAppointments;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new Role[]{
                this.role
        });
    }

    @Override
    public String getPassword() {
        return this.doctorPassword;
    }

    @Override
    public String getUsername() {
        return this.doctorName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
