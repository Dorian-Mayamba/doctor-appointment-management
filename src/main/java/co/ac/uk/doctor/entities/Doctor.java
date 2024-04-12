package co.ac.uk.doctor.entities;

import co.ac.uk.doctor.generic.IUserDetails;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
@Entity
@Table(name = "doctors")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Doctor extends User implements IUserDetails {


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

    @Column(nullable = false)
    private String doctorPassword;

    @Column(nullable = false)
    private String speciality;

    @Column(nullable = false)
    private String doctorNumber;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.EAGER)
    List<Appointment> doctorAppointments;

    public List<Appointment> getDoctorAppointments() {
        return doctorAppointments;
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
