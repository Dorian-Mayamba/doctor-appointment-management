package co.ac.uk.doctor.constants;

public class CredentialConstant {
    public static final String ADMIN_EMAIL = "Admin@admin.ac.co.uk";
    public static final String ADMIN_NAME = "Admin";
    public static final String ADMIN_PASSWORD = "123123";

    public static final String PATIENT_EMAIL = "Patient@patient.ac.co.uk";
    public static final String PATIENT_NAME = "Patient";
    public static final String PATIENT_PASSWORD = "123123";

    public static final String DOCTOR_EMAIL = "Doctor@doctor.ac.co.uk";
    public static final String DOCTOR_NAME = "Doctor";
    public static final String DOCTOR_PASSWORD = "123123";

    public static enum Roles{
        Admin,
        Patient,
        Doctor
    };
}
