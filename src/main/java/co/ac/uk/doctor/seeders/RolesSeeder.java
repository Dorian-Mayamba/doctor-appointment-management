package co.ac.uk.doctor.seeders;

import co.ac.uk.doctor.constants.RoleConstants;
import co.ac.uk.doctor.userdetails.Role;
import co.ac.uk.doctor.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.util.Arrays;

public class RolesSeeder implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (!(roleRepository.count() > 0)){
            roleRepository.saveAll(Arrays.asList(new Role(RoleConstants.ADMIN), new Role(RoleConstants.DOCTOR), new Role(RoleConstants.PATIENT)));
        }
    }
}
