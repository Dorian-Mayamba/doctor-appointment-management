package co.ac.uk.doctor.controllers;


import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {
    @GetMapping
    public String greet(Authentication authentication){
        return authentication != null ? authentication.getName() : "Hello world";
    }

    @GetMapping("/admin")
    public String greetAdmin(Authentication authentication){
        return authentication != null ? String.format("Hello admin %s", authentication.getName()) : "Hello world";
    }
}
