package co.ac.uk.doctor.seeders;

import co.ac.uk.doctor.constants.CredentialConstant;
import co.ac.uk.doctor.entities.Doctor;
import co.ac.uk.doctor.repositories.DoctorRepository;
import co.ac.uk.doctor.services.RoleService;
import co.ac.uk.doctor.utils.RoleCheckerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DoctorSeeder implements CommandLineRunner {

    @Autowired
    DoctorRepository repository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RoleService roleService;

    @Override
    public void run(String... args) throws Exception {
        if (!(repository.count() > 0)){
            repository.save(new Doctor(CredentialConstant.DOCTOR_NAME,
                    CredentialConstant.DOCTOR_EMAIL,
                    encoder.encode(CredentialConstant.DOCTOR_PASSWORD),
                    roleService.findByType(RoleCheckerUtil.checkRoleByEmail(CredentialConstant.DOCTOR_EMAIL)), "Dentist"));
        }
    }
}
