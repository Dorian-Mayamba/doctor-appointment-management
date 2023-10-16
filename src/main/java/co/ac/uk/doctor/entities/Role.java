package co.ac.uk.doctor.entities;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
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
    private List<User> users;

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

    public Role addUser(User user){
        this.users.add(user);
        return this;
    }

    public Role(String type){
        this.setAuthority(type);
    }

    public List<User> getUsers() {
        return users;
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
