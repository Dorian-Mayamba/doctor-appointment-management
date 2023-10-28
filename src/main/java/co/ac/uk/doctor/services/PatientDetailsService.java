package co.ac.uk.doctor.services;

import co.ac.uk.doctor.entities.Patient;
import co.ac.uk.doctor.generic.IUserDetailsService;
import co.ac.uk.doctor.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class PatientDetailsService implements IUserDetailsService {

    @Autowired
    PatientRepository patientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return patientRepository.getPatientByPatientEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Could not find username: " + username));
    }

    @Override
    public UserDetails loadUserById(Long id) {
        return this.patientRepository
                .findById(id)
                .orElseThrow(()->new UsernameNotFoundException("Not found"));
    }

    public Patient savePatient(Patient patient) {
        return this.patientRepository.save(patient);
    }
}
