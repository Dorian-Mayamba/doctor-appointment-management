package co.ac.uk.doctor.seeders;

import co.ac.uk.doctor.constants.CredentialConstant;
import co.ac.uk.doctor.userdetails.Doctor;
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
            repository.save(
                    Doctor.builder().doctorName(CredentialConstant.DOCTOR_NAME)
                            .doctorEmail(CredentialConstant.DOCTOR_EMAIL)
                            .doctorNumber(CredentialConstant.DOCTOR_NUMBER)
                            .doctorPassword(encoder.encode(CredentialConstant.DOCTOR_PASSWORD))
                            .speciality("Dentist")
                            .role(roleService.findByType(RoleCheckerUtil.checkRoleByEmail(CredentialConstant.DOCTOR_EMAIL)))
                            .build()
            );
        }
    }
}
