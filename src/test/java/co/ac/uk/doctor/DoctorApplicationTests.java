package co.ac.uk.doctor;

import co.ac.uk.doctor.repositories.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    }

    @Test
    @Order(2)
    public void createAdminTest(){
        Role role = this.roleRepository.findRoleByType(CredentialConstant.Roles.Admin.name());
        if(role == null){
            throw new IllegalStateException("Role should not be null");
        }
        Admin admin = new Admin(CredentialConstant.ADMIN_NAME,
                CredentialConstant.ADMIN_EMAIL, passwordEncoder.encode(CredentialConstant.ADMIN_PASSWORD), role);
        if(adminRepository.getAdminByAdminEmail(admin.getAdminEmail()).isPresent()){
            return;
        }
        Admin savedAdmin = this.adminRepository.save(admin);
        Assertions.assertNotNull(savedAdmin);
    }

    @Test
    @Order(3)
    public void createDoctorTest(){
        Role role = this.roleRepository.findRoleByType(CredentialConstant.Roles.Doctor.name());
        if(role == null){
            throw new IllegalStateException("Role should not be null");
        }
        Doctor doctor = new Doctor(CredentialConstant.DOCTOR_NAME,CredentialConstant.DOCTOR_EMAIL, passwordEncoder.encode(CredentialConstant.DOCTOR_PASSWORD), role);
        if(doctorRepository.getDoctorByDoctorEmail(doctor.getDoctorEmail()).isPresent()){
            return;
        }
        Doctor savedDoctor = this.doctorRepository.save(doctor);
        Assertions.assertNotNull(savedDoctor);
    }

    @Test
    @Order(4)
    public void createPatientTest(){
        Role role = this.roleRepository.findRoleByType(CredentialConstant.Roles.Patient.name());
        if(role == null){
            throw new IllegalStateException("Role should not be null");
        }
        Patient patient = new Patient(CredentialConstant.PATIENT_NAME, CredentialConstant.PATIENT_EMAIL, passwordEncoder.encode(CredentialConstant.PATIENT_PASSWORD), role, "07404566479");
        if(patientRepository.getPatientByPatientEmail(patient.getPatientEmail()).isPresent()){
            return;
        }
        Patient savedPatient = this.patientRepository.save(patient);
        Assertions.assertNotNull(savedPatient);
    }

    @Test
    @Order(5)
    public void makeAppointmentTest(){
        LocalDate localDate = LocalDate.of(2023,10,26);
        LocalTime localTime = LocalTime.of(1,0,0);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_TIME;
        String time = localTime.format(timeFormatter);
        String date = localDate.toString();
        Optional<Doctor> doctorByName = this.doctorRepository.getDoctorByDoctorEmail(CredentialConstant.DOCTOR_EMAIL);
        Optional<Patient> patientByName = this.patientRepository.getPatientByPatientEmail(CredentialConstant.PATIENT_EMAIL);
        if(doctorByName.isPresent() && patientByName.isPresent()){
            Doctor doctor =  doctorByName.get();
            Patient patient = patientByName.get();
            Appointment appointment = new Appointment(date,time,patient,doctor);
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
