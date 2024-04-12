package co.ac.uk.doctor.entities;

import co.ac.uk.doctor.generic.IUserDetails;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;



@Entity
@Table(name = "patients")
public class Patient extends User implements IUserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String patientName;

    @Column(nullable = false, unique = true)
    private String patientEmail;

    @Column(nullable = false)
    private String patientNumber;

    public String getPatientNumber() {
        return patientNumber;
    }

    public void setPatientNumber(String patientNumber) {
        this.patientNumber = patientNumber;
    }

    @Column(nullable = false)
    private String patientPassword;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public void setPatientPassword(String patientPassword) {
        this.patientPassword = patientPassword;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Patient(String patientName, String patientEmail, String patientPassword, Role role, String number) {
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.patientPassword = patientPassword;
        this.role = role;
        this.patientNumber = number;
    }

    public Patient(){
        super();
    }

    @OneToMany(mappedBy = "patient", fetch = FetchType.EAGER)
    List<Appointment> patientAppointments;

    public List<Appointment> getPatientAppointments() {
        return patientAppointments;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Role getRole() {
        return this.role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new Role[]{
                this.role
        });
    }

    @Override
    public String getPassword() {
        return this.patientPassword;
    }

    public String getPatientName() {
        return this.patientName;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    @Override
    public String getUsername() {
        return this.patientName;
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
