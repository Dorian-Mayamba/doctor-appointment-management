package co.ac.uk.doctor.entities;

import co.ac.uk.doctor.entities.generic.IUserDetails;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
@Entity
@Table(name = "doctors")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Doctor extends User implements IUserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String speciality;

    @Column(nullable = false)
    private String number;
    @Column
    private String profile;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    List<Appointment> appointments;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    List<Rating> ratings;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    List<Review> reviews;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    List<Slot> slots;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new Role[]{
                this.role
        });
    }


    @Override
    public String getUsername() {
        return this.email;
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
