package co.ac.uk.doctor.responses;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddPatientResponse {
    private String message;

    private boolean success;
}
