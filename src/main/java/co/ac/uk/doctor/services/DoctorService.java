package co.ac.uk.doctor.services;

import co.ac.uk.doctor.constants.CredentialConstant;
import co.ac.uk.doctor.entities.Doctor;
import co.ac.uk.doctor.exceptions.AlreadyRegisteredUserException;
import co.ac.uk.doctor.serializers.DoctorSerializer;
import co.ac.uk.doctor.services.generic.AbstractUserDetailsService;
import co.ac.uk.doctor.entities.generic.IUserDetails;
import co.ac.uk.doctor.repositories.DoctorRepository;
import co.ac.uk.doctor.requests.AddDoctorRequest;
import co.ac.uk.doctor.requests.AddUserRequest;
import co.ac.uk.doctor.requests.EditDoctorRequest;
import co.ac.uk.doctor.requests.EditUserRequest;
import co.ac.uk.doctor.utils.EntityToSerializerConverter;
import co.ac.uk.doctor.utils.RoleCheckerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService extends AbstractUserDetailsService<Doctor> {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return doctorRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Could not find " + username));
    }

    @Override
    public Doctor findByEmail(String email) {
        return doctorRepository
                .findByEmail(email)
                .orElse(null);
    }

    @Override
    public UserDetails loadUserById(Long id) {
        return this.doctorRepository
                .findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));
    }

    @Override
    public void checkUserInDatabase(String username) throws AlreadyRegisteredUserException {
        IUserDetails userDetails = (IUserDetails) loadUserByUsername(username);
        if (userDetails != null) {
            throw new AlreadyRegisteredUserException("The doctor " + username + " is already in the database");
        }
    }

    @Override
    public Doctor deleteUser(Doctor user) {
        doctorRepository.delete(user);
        return user;
    }

    @Override
    public List<Doctor> getUsers() {
        return this.doctorRepository.findAll();
    }

    public List<DoctorSerializer> findBySpeciality(String speciality){
        return EntityToSerializerConverter
                .toDoctorSerializer(doctorRepository.findBySpeciality(speciality));
    }

    @Override
    public Doctor removeUser(Long userId) {
        Doctor doctorToDelete = (Doctor) loadUserById(userId);
        doctorRepository.delete(doctorToDelete);
        return doctorToDelete;
    }

    @Override
    public Doctor editUser(Long userId, EditUserRequest request) {
        Doctor doctorToEdit = (Doctor) loadUserById(userId);
        if (request instanceof EditDoctorRequest) {
            EditDoctorRequest editDoctorRequest = (EditDoctorRequest) request;
            doctorToEdit.setName(editDoctorRequest.getDoctorName());
            doctorToEdit.setEmail(editDoctorRequest.getDoctorEmail());
            doctorToEdit.setSpeciality(editDoctorRequest.getDoctorSpeciality());
            doctorToEdit.setNumber(editDoctorRequest.getDoctorNumber());
            return saveDoctor(doctorToEdit);
        }
        return null;
    }

    @Override
    public Doctor addUser(AddUserRequest request) throws AlreadyRegisteredUserException {
        try {
            if (request instanceof AddDoctorRequest) {
                AddDoctorRequest addDoctorRequest = (AddDoctorRequest) request;
                checkUserInDatabase(addDoctorRequest.getDoctorEmail());
            }
        } catch (UsernameNotFoundException ex) {
            return saveUser(ex, new Doctor(), request);
        }
        throw new IllegalStateException("The class " + request.getClass().toString() + " Should extend the AddUserRequest class");
    }

    @Override
    public Doctor saveUser(Exception exception, Doctor user, AddUserRequest request) {
        if (request instanceof AddDoctorRequest && exception instanceof UsernameNotFoundException) {
            Doctor doctor = user;
            AddDoctorRequest addDoctorRequest = (AddDoctorRequest) request;
            doctor.setName(addDoctorRequest.getDoctorName());
            doctor.setEmail(addDoctorRequest.getDoctorEmail());
            doctor.setSpeciality(addDoctorRequest.getDoctorSpeciality());
            doctor.setNumber(addDoctorRequest.getDoctorNumber());
            doctor.setPassword(encoder.encode(CredentialConstant.DOCTOR_PASSWORD));
            doctor.setRole(getRole(RoleCheckerUtil.checkRoleByEmail(doctor.getEmail())));
            return this.doctorRepository.save(doctor);
        }
        return null;
    }

    public Doctor saveDoctor(Doctor doctor) {
        return this.doctorRepository.save(doctor);
    }
}
