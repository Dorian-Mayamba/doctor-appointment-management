package co.ac.uk.doctor.controllers;

import co.ac.uk.doctor.entities.User;
import co.ac.uk.doctor.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }
    @GetMapping
    public Iterable<User> findAll(){
        log.info("Find all function call");
        return this.userService.findAll();
    }
}
