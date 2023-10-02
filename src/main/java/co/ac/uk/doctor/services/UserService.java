package co.ac.uk.doctor.services;

import co.ac.uk.doctor.entities.User;
import co.ac.uk.doctor.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Iterable<User> findAll(){
        return this.userRepository.findAll();
    }

    public User save(User user){
        return this.userRepository.save(user);
    }

    public Optional<User> findById(Long id){
        return this.userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email){
        return this.userRepository.findByEmail(email);
    }

    public void deleteById(Long id){
        this.userRepository.deleteById(id);
    }

    public User updateById(Long id, User user){
        User userToUpdate = this.userRepository.findById(id).get();
        userToUpdate.setEmail(userToUpdate.getEmail().equals(user.getEmail())
                ? userToUpdate.getEmail() : user.getEmail());
        userToUpdate.setPassword(userToUpdate.getPassword().equals(user.getPassword())
                ? userToUpdate.getPassword() : user.getPassword());
        userToUpdate.setName(userToUpdate.getName().equals(user.getEmail())
                ? userToUpdate.getName() : user.getName());
        return this.userRepository.save(userToUpdate);
    }

    public Optional<User> findByName(String name){
        return this.userRepository.findByName(name);
    }


}
