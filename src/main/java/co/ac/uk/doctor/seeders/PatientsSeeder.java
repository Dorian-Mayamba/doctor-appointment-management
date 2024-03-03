package co.ac.uk.doctor.seeders;

import co.ac.uk.doctor.constants.CredentialConstant;
import co.ac.uk.doctor.entities.jpa.Patient;
import co.ac.uk.doctor.repositories.jpa.PatientRepository;
import co.ac.uk.doctor.services.RoleService;
import co.ac.uk.doctor.utils.RoleCheckerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PatientsSeeder implements CommandLineRunner {
    @Autowired
    PatientRepository patientRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RoleService roleService;

    @Override
    public void run(String... args) throws Exception {
        if (!(patientRepository.count() > 0)){
            patientRepository.save(new Patient(CredentialConstant.PATIENT_NAME,CredentialConstant.PATIENT_EMAIL,
                    encoder.encode(CredentialConstant.PATIENT_PASSWORD),
                    roleService.findByType(RoleCheckerUtil.checkRoleByEmail(CredentialConstant.PATIENT_EMAIL)),CredentialConstant.PATIENT_NUMBER));
        }
    }
}
