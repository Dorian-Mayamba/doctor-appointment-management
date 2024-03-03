package co.ac.uk.doctor.seeders;

import co.ac.uk.doctor.constants.CredentialConstant;
import co.ac.uk.doctor.entities.jpa.Admin;
import co.ac.uk.doctor.repositories.jpa.AdminRepository;
import co.ac.uk.doctor.services.RoleService;
import co.ac.uk.doctor.utils.RoleCheckerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AdminSeeder implements CommandLineRunner {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RoleService roleService;

    @Override
    public void run(String... args) throws Exception {
        if(!(adminRepository.count() > 0)){
            adminRepository.save(new Admin(CredentialConstant.ADMIN_NAME,
                    CredentialConstant.ADMIN_EMAIL,
                    encoder.encode(CredentialConstant.ADMIN_PASSWORD),
                    roleService.findByType(RoleCheckerUtil.checkRoleByEmail(CredentialConstant.ADMIN_EMAIL))));
        }
    }
}
