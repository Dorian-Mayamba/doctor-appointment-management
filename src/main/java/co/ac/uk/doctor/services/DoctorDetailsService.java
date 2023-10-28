package co.ac.uk.doctor.services;

import co.ac.uk.doctor.entities.Doctor;
import co.ac.uk.doctor.generic.IUserDetailsService;
import co.ac.uk.doctor.repositories.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DoctorDetailsService implements IUserDetailsService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return doctorRepository
                .getDoctorByDoctorEmail(username)
                .orElseThrow(()->new UsernameNotFoundException("Could not find "+username));
    }

    @Override
    public UserDetails loadUserById(Long id) {
        return this.doctorRepository
                .findById(id)
                .orElseThrow(()->new UsernameNotFoundException("Not found"));
    }

    public Doctor saveDoctor(Doctor doctor) {
        return this.doctorRepository.save(doctor);
    }
}
