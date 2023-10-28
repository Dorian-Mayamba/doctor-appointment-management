package co.ac.uk.doctor.services;

import co.ac.uk.doctor.entities.Admin;
import co.ac.uk.doctor.generic.IUserDetailsService;
import co.ac.uk.doctor.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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

    public Admin saveAdmin(Admin admin){
        return this.adminRepository.save(admin);
    }
}
