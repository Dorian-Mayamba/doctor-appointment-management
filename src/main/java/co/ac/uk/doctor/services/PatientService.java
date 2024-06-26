package co.ac.uk.doctor.services;

import co.ac.uk.doctor.entities.Patient;
import co.ac.uk.doctor.exceptions.AlreadyRegisteredUserException;
import co.ac.uk.doctor.services.generic.AbstractUserDetailsService;
import co.ac.uk.doctor.entities.generic.IUserDetails;
import co.ac.uk.doctor.repositories.PatientRepository;
import co.ac.uk.doctor.requests.*;
import co.ac.uk.doctor.utils.RoleCheckerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService extends AbstractUserDetailsService<Patient> {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return patientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Could not find username: " + username));
    }

    @Override
    public UserDetails loadUserById(Long id) {
        return this.patientRepository
                .findById(id)
                .orElseThrow(()->new UsernameNotFoundException("Not found"));
    }

    @Override
    public Patient findByEmail(String email) {
        return patientRepository
                .findByEmail(email)
                .orElse(null);
    }

    @Override
    public void checkUserInDatabase(String username) throws AlreadyRegisteredUserException{
        IUserDetails userDetails = (IUserDetails) loadUserByUsername(username);
        if (userDetails != null){
            throw new AlreadyRegisteredUserException("The patient "+ username + " is already in the database");
        }
    }

    @Override
    public List<Patient> getUsers() {
        List<Patient> patients = new ArrayList<>();
        for (Patient patient:patientRepository.findAll()){
            patients.add(patient);
        }
        return patients;
    }

    @Override
    public Patient deleteUser(Patient user) {
        patientRepository.delete(user);
        return user;
    }

    @Override
    public Patient removeUser(Long userId) {
        Patient patient = (Patient) loadUserById(userId);
        patientRepository.delete(patient);
        return patient;
    }

    @Override
    public Patient editUser(Long userId, EditUserRequest request) {
        Patient patientToEdit = (Patient) loadUserById(userId);
        if (request instanceof EditPatientRequest){
            EditPatientRequest editPatientRequest = (EditPatientRequest) request;
            patientToEdit.setName(editPatientRequest.getName());
            patientToEdit.setEmail(editPatientRequest.getEmail());
            patientToEdit.setNumber(editPatientRequest.getNumber());
            patientToEdit.setPassword(encoder.encode(editPatientRequest.getPassword()));
            return savePatient(patientToEdit);
        }
        return null;
    }

    @Override
    public Patient addUser(AddUserRequest request) throws AlreadyRegisteredUserException {
        try{
            if (request instanceof AddPatientRequest){
                AddPatientRequest addPatientRequest = (AddPatientRequest) request;
                checkUserInDatabase(addPatientRequest.getEmail());
            }
        }catch (UsernameNotFoundException exception){
            return saveUser(exception,new Patient(), request);
        }
        throw new IllegalStateException("The class "+ request.getClass().toString() + " Should extend the AddUserRequest class");
    }

    @Override
    public Patient saveUser(Exception exception, Patient user, AddUserRequest request) {
        Patient patient = user;
        if (exception instanceof UsernameNotFoundException && request instanceof AddPatientRequest){
            AddPatientRequest addPatientRequest = (AddPatientRequest) request;
            patient.setName(addPatientRequest.getName());
            patient.setEmail(addPatientRequest.getEmail());
            patient.setNumber(addPatientRequest.getNumber());
            patient.setPassword(encoder.encode(addPatientRequest.getPassword()));
            patient.setRole(getRole(RoleCheckerUtil.checkRoleByEmail(patient.getEmail())));
        }
        return this.savePatient(patient);
    }

    public Patient savePatient(Patient patient) {
        return this.patientRepository.save(patient);
    }
}
