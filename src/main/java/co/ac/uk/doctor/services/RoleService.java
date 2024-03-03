package co.ac.uk.doctor.services;

import co.ac.uk.doctor.entities.jpa.Role;
import co.ac.uk.doctor.repositories.jpa.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }
    public Role findByType(String type){
        return this.roleRepository.findRoleByType(type);
    }

    public Role findRoleById(Long id){
        return this.roleRepository.findRoleById(id);
    }
}
