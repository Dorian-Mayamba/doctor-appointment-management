package co.ac.uk.doctor.services;

import co.ac.uk.doctor.entities.Admin;
import co.ac.uk.doctor.exceptions.AlreadyRegisteredUserException;
import co.ac.uk.doctor.generic.IUserDetails;
import co.ac.uk.doctor.generic.IUserDetailsService;
import co.ac.uk.doctor.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class AdminDetailsService implements IUserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return adminRepository.getAdminByAdminEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("Could not find admin email: "+ username));
    }

    @Override
    public UserDetails loadUserById(Long id) {
        return this.adminRepository
                .findById(id)
                .orElseThrow(()->new UsernameNotFoundException("Not found"));
    }

    @Override
    public void checkUserInDatabase(String username) throws AlreadyRegisteredUserException {
        IUserDetails userDetails = (IUserDetails) loadUserByUsername(username);
        if (userDetails != null){
            throw new AlreadyRegisteredUserException("The admin "+ username + " is already in the database");
        }
    }

    @Override
    public List<IUserDetails> getUsers() {
        List<IUserDetails> iUserDetails = new ArrayList<>();
        for (Admin admin:adminRepository.findAll()){
            iUserDetails.add(admin);
        }
        return iUserDetails;
    }

    public Admin saveAdmin(Admin admin){
        return this.adminRepository.save(admin);
    }
}
