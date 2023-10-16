package co.ac.uk.doctor.services;

import co.ac.uk.doctor.entities.Role;
import co.ac.uk.doctor.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
