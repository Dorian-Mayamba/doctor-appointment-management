package co.ac.uk.doctor.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AddPatientRequest extends AddUserRequest{
    private String email, name, password, number;
}
