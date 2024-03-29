package co.ac.uk.doctor.requests;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class AddDoctorRequest extends AddUserRequest{
    private String doctorName;

    private String doctorEmail;

    private String doctorSpeciality;

    private String doctorNumber;
}
