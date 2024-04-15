package co.ac.uk.doctor.userdetails;


import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String type;

    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER)
    private List<Patient> patients;

    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER)
    private List<Doctor> doctors;

    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER)
    private List<Admin> admins;
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Role(String type){
        this.type = type;
        this.setAuthority(this.type);
    }

    public Role(){}

    @Override
    public String getAuthority() {
        return this.getType();
    }


    public void setAuthority(String authority){
        this.setType(authority);
    }

}
