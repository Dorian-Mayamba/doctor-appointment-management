package co.ac.uk.doctor.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditDoctorRequest extends EditUserRequest{
    private String doctorName;

    private String doctorEmail;

    private String doctorSpeciality;

    private String doctorPassword;

    private String doctorNumber;
}
