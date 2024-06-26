package co.ac.uk.doctor.constants;

public class CredentialConstant {
    public static final String ADMIN_EMAIL = "Admin@admin.co.ac.uk";
    public static final String ADMIN_NAME = "Admin";
    public static final String ADMIN_PASSWORD = "123123";
    public static final String ADMIN_NUMBER = "07717647374";


    public static final String PATIENT_EMAIL = "Patient@patient.co.ac.uk";
    public static final String PATIENT_NAME = "Patient";
    public static final String PATIENT_PASSWORD = "123123";

    public static final String PATIENT_NUMBER = "07717647375";

    public static final String DOCTOR_EMAIL = "Doctor@doctor.co.ac.uk";
    public static final String DOCTOR_NAME = "Doctor";
    public static final String DOCTOR_PASSWORD = "123123";
    public static final String DOCTOR_NUMBER = "07717647376";


    public static enum Roles{
        Admin,
        Patient,
        Doctor
    };
}
