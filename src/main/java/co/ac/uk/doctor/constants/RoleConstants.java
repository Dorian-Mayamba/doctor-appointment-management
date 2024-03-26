package co.ac.uk.doctor.constants;

import java.util.HashMap;
import java.util.Map;

public class RoleConstants {
    public static final String ADMIN = "Admin";
    public static final String DOCTOR = "Doctor";
    public static final String PATIENT = "Patient";

    public static Map<String, Long> getRoles(){
        Map<String, Long> roles = new HashMap<>();
        roles.put(ADMIN, 1L);
        roles.put(DOCTOR,2L);
        roles.put(PATIENT, 3L);
        return roles;
    }
}
