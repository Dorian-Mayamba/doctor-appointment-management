package co.ac.uk.doctor.services;

import co.ac.uk.doctor.entities.Admin;
import co.ac.uk.doctor.exceptions.AlreadyRegisteredUserException;
import co.ac.uk.doctor.services.generic.AbstractUserDetailsService;
import co.ac.uk.doctor.entities.generic.IUserDetails;
import co.ac.uk.doctor.repositories.AdminRepository;
import co.ac.uk.doctor.requests.AddAdminRequest;
import co.ac.uk.doctor.requests.AddUserRequest;
import co.ac.uk.doctor.requests.EditAdminRequest;
import co.ac.uk.doctor.requests.EditUserRequest;
import co.ac.uk.doctor.utils.RoleCheckerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class AdminDetailsService extends AbstractUserDetailsService<Admin> {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    PasswordEncoder encoder;

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
    public List<Admin> getUsers() {
        List<Admin> admins = new ArrayList<>();
        for (Admin admin:adminRepository.findAll()){
            admins.add(admin);
        }
        return admins;
    }

    @Override
    public Admin removeUser(Long userId) {
        Admin adminToDelete = (Admin) this.loadUserById(userId);
        adminRepository.delete(adminToDelete);
        return adminToDelete;
    }

    @Override
    public Admin editUser(Long userId, EditUserRequest request) {
        Admin admin = (Admin)loadUserById(userId);
        if (request instanceof EditAdminRequest){
            EditAdminRequest editAdminRequest = (EditAdminRequest) request;
            admin.setAdminEmail(editAdminRequest.getAdminEmail());
            admin.setAdminName(editAdminRequest.getAdminName());
            admin.setAdminPassword(encoder.encode(editAdminRequest.getAdminPassword()));
            return saveAdmin(admin);
        }
        return null;
    }

    @Override
    public Admin addUser(AddUserRequest request) throws AlreadyRegisteredUserException{
        try {
            if (request instanceof AddAdminRequest){
                AddAdminRequest addAdminRequest = (AddAdminRequest) request;
                checkUserInDatabase(addAdminRequest.getAdminEmail());
            }
        }catch (UsernameNotFoundException exception){
            saveUser(exception,new Admin(), request);
        }
        throw new IllegalStateException("The class "+ request.getClass().toString() + " Should extend the AddUserRequest class");
    }

    @Override
    public Admin saveUser(Exception exception, Admin user, AddUserRequest request) {
        Admin admin = user;
        if (request instanceof AddAdminRequest && exception instanceof UsernameNotFoundException){
            admin.setAdminName(((AddAdminRequest)request).getAdminName());
            admin.setAdminEmail(((AddAdminRequest)request).getAdminEmail());
            admin.setAdminPassword(encoder.encode(((AddAdminRequest)request).getAdminPassword()));
            admin.setRole(getRole(RoleCheckerUtil.checkRoleByEmail(admin.getAdminEmail())));
        }
        return saveAdmin(admin);
    }

    public Admin saveAdmin(Admin admin){
        return this.adminRepository.save(admin);
    }
}
