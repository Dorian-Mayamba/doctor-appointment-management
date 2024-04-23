package co.ac.uk.doctor.seeders;

import co.ac.uk.doctor.constants.CredentialConstant;
import co.ac.uk.doctor.constants.RoleConstants;
import co.ac.uk.doctor.entities.Admin;
import co.ac.uk.doctor.repositories.AdminRepository;
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
            adminRepository.save(Admin.builder()
                    .name(CredentialConstant
                            .ADMIN_NAME)
                            .email(CredentialConstant.ADMIN_EMAIL)
                            .password(encoder.encode(CredentialConstant.ADMIN_PASSWORD))
                            .number(CredentialConstant.ADMIN_NUMBER)
                            .role(roleService.findByType(RoleCheckerUtil.checkRoleByEmail(CredentialConstant.ADMIN_EMAIL)))
                    .build());
        }
    }
}
