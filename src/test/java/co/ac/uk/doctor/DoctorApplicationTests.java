package co.ac.uk.doctor;

import co.ac.uk.doctor.entities.Appointment;
import co.ac.uk.doctor.entities.Role;
import co.ac.uk.doctor.entities.User;
import co.ac.uk.doctor.repositories.AppointmentRepository;
import co.ac.uk.doctor.repositories.RoleRepository;
import co.ac.uk.doctor.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DoctorApplicationTests {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    AppointmentRepository appointmentRepository;
    @Test
    void contextLoads() {
    }

    @Test
    @Order(1)
    public void createRoleTest(){
        List<Role> roles = Arrays.asList(new Role("Admin"),
                new Role("User"),
                new Role("Patient"),
                new Role("Doctor"));
        Iterable<Role> savedRoles = this.roleRepository.saveAll(roles);
        Assertions.assertNotNull(savedRoles);
    }

    @Test
    @Order(2)
    public void createAdminTest(){
        Role role = roleRepository.findRoleByType("Admin");
        if(role == null){
            throw new IllegalStateException("null role found");
        }
        User user = new User("Dorian","dorian.mayamba@gmail.com", passwordEncoder.encode("Dodoetienne01"),role);
        role.addUser(user);
        roleRepository.save(role);
        userRepository.save(user);
    }

    @Test
    @Order(3)
    public void loadUserTest(){
        User user = (User) userDetailsService.loadUserByUsername("dorian.mayamba@gmail.com");
        Assertions.assertEquals("Admin", user.getRole().getType());
    }

    @Test
    @Order(4)
    public void loadAdminsTest(){
        Role role = roleRepository.findRoleByType("Admin");
        Assertions.assertEquals(1,role.getUsers().size());
    }

    @Test
    @Order(5)
    public void createAppointmentTest(){
        User user = (User) this.userDetailsService.loadUserByUsername("dorian.mayamba@gmail.com");
        Appointment appointment = new Appointment(LocalDate.of(2023,10,20).toString(), LocalTime.of(1,0,0).toString(), user);
        updateAppointmentRecords(appointment,user);
        Appointment savedAppointment = this.appointmentRepository.save(appointment);
        Assertions.assertNotNull(savedAppointment);
    }

    private void updateAppointmentRecords(Appointment appointment, User user){
        if(user.getRole().getType().equals("Doctor")){
            appointment.setPatientName("marcel");
            return;
        }
        appointment.setDoctorName("Alain");
    }

}
