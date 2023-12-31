package co.ac.uk.doctor.entities;

import co.ac.uk.doctor.generic.IUserDetails;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;



@Entity
@Table(name = "patients")
public class Patient implements IUserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String patientName;

    @Column(nullable = false, unique = true)
    private String patientEmail;

    @Column(nullable = false)
    private String patientPassword;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    public Patient(String patientName, String patientEmail, String patientPassword, Role role) {
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.patientPassword = patientPassword;
        this.role = role;
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
        return null;
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
