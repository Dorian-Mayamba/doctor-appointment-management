package co.ac.uk.doctor.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class EditPatientRequest extends EditUserRequest{
    private String name, email, number, password;
}
