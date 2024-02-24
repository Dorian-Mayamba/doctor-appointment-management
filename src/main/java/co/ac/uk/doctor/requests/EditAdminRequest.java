package co.ac.uk.doctor.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class EditAdminRequest extends EditUserRequest{
    private String adminName, adminEmail,adminPassword;
}
