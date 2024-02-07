package co.ac.uk.doctor.services;

import co.ac.uk.doctor.entities.Doctor;
import co.ac.uk.doctor.exceptions.AlreadyRegisteredUserException;
import co.ac.uk.doctor.generic.IUserDetails;
import co.ac.uk.doctor.generic.IUserDetailsService;
import co.ac.uk.doctor.repositories.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void checkUserInDatabase(String username) throws AlreadyRegisteredUserException{
        try{
            IUserDetails userDetails = (IUserDetails) loadUserByUsername(username);
            if (userDetails != null){
                throw new AlreadyRegisteredUserException("The doctor "+ username + " is already in the database");
            }
        }catch (UsernameNotFoundException ex){
            //Ignore as in this catching this exception means that there is no user
        }

    }

    @Override
    public List<IUserDetails> getUsers() {
        List<IUserDetails> userDetails = new ArrayList<>();
        for (Doctor doctor:doctorRepository.findAll()){
            userDetails.add(doctor);
        }
        return userDetails;
    }

    public Doctor saveDoctor(Doctor doctor) {
        return this.doctorRepository.save(doctor);
    }
}
