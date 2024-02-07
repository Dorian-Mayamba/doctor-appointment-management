package co.ac.uk.doctor.utils;

import co.ac.uk.doctor.constants.RoleConstants;

public class RoleCheckerUtil {
    public static String checkRoleByEmail(String username) {
        if (username.contains("@admin")) {
            return RoleConstants.ADMIN;
        } else if (username.contains("@doctor")) {
            return RoleConstants.DOCTOR;
        } else {
            return RoleConstants.PATIENT;
        }
    }
}

