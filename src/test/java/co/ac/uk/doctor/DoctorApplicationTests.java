package co.ac.uk.doctor;

import co.ac.uk.doctor.constants.CredentialConstant;
import co.ac.uk.doctor.entities.*;
import co.ac.uk.doctor.repositories.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.print.Doc;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DoctorApplicationTests {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;
    @Test
    void contextLoads() {
    }

    /*@Test
    @Order(1)
    public void createRoleTest(){
        List<Role> roles = Arrays.asList(new Role("Admin"),
                new Role("Doctor"),
                new Role("Patient"));
        if(this.roleRepository.findRoleByType("Admin") != null){
            return;
        }
        Iterable<Role> savedRoles = this.roleRepository.saveAll(roles);
        Assertions.assertNotNull(savedRoles);
    }*/

    /*@Test
    @Order(2)
    public void createAdminTest(){
        Role role = this.roleRepository.findRoleByType(CredentialConstant.Roles.Admin.name());
        if(role == null){
            throw new IllegalStateException("Role should not be null");
        }
        Admin admin = Admin.builder()
                .name(CredentialConstant.ADMIN_NAME)
                .email(CredentialConstant.ADMIN_EMAIL)
                .password(passwordEncoder.encode(CredentialConstant.ADMIN_PASSWORD))
                .number(CredentialConstant.ADMIN_NUMBER)
                .role(role)
                .build();
        if(adminRepository.findByEmail(admin.getEmail()).isPresent()){
            return;
        }
        Admin savedAdmin = this.adminRepository.save(admin);
        Assertions.assertNotNull(savedAdmin);
    }*/

    /*@Test
    @Order(3)
    public void createDoctorTest(){
        Role role = this.roleRepository.findRoleByType(CredentialConstant.Roles.Doctor.name());
        if(role == null){
            throw new IllegalStateException("Role should not be null");
        }
        Doctor doctor = Doctor
                .builder()
                .name(CredentialConstant.DOCTOR_NAME)
                .email(CredentialConstant.DOCTOR_EMAIL)
                .password(passwordEncoder.encode(CredentialConstant.DOCTOR_PASSWORD))
                .number(CredentialConstant.DOCTOR_NUMBER)
                .role(role)
                .build();
        if(doctorRepository.findByEmail(doctor.getEmail()).isPresent()){
            return;
        }
        Doctor savedDoctor = this.doctorRepository.save(doctor);
        Assertions.assertNotNull(savedDoctor);
    }*/

    /*@Test
    @Order(4)
    public void createPatientTest(){
        Role role = this.roleRepository.findRoleByType(CredentialConstant.Roles.Patient.name());
        if(role == null){
            throw new IllegalStateException("Role should not be null");
        }
        Patient patient = Patient.builder()
                .name(CredentialConstant.PATIENT_NAME)
                .email(CredentialConstant.PATIENT_EMAIL)
                .password(passwordEncoder.encode(CredentialConstant.PATIENT_PASSWORD))
                .number(CredentialConstant.PATIENT_NUMBER)
                .role(role).build();
        if(patientRepository.findByEmail(patient.getEmail()).isPresent()){
            return;
        }
        Patient savedPatient = this.patientRepository.save(patient);
        Assertions.assertNotNull(savedPatient);
    }*/

    /*@Test
    @Order(5)
    public void makeAppointmentTest(){
        LocalDate localDate = LocalDate.of(2023,10,26);
        LocalTime localTime = LocalTime.of(1,0,0);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_TIME;
        String time = localTime.format(timeFormatter);
        String date = localDate.toString();
        Optional<Doctor> doctorByName = this.doctorRepository.findByEmail(CredentialConstant.DOCTOR_EMAIL);
        Optional<Patient> patientByName = this.patientRepository.findByEmail(CredentialConstant.PATIENT_EMAIL);
        if(doctorByName.isPresent() && patientByName.isPresent()){
            Doctor doctor =  doctorByName.get();
            Patient patient = patientByName.get();
            Appointment appointment = Appointment
                    .builder()
                    .doctor(doctor)
                    .patient(patient)
                    .date(localDate)
                    .time(localTime)
                    .endTime(localTime.plusHours(1))
                    .status(Appointment.Status.PENDING)
                    .build();
            if (appointmentRepository.getAppointmentByDoctorIdAndPatientId(doctor.getId(),
                    patient.getId()).isPresent()){
                return;
            }
            Appointment savedAppointment = this.appointmentRepository.save(appointment);
            doctor.getDoctorAppointments().add(savedAppointment);
            patient.getPatientAppointments().add(savedAppointment);
            Assertions.assertNotNull(savedAppointment);
        }
    }*/
}
