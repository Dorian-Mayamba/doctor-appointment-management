package co.ac.uk.doctor.serializers;

import lombok.Data;

import java.util.List;

@Data
public class PatientSerializer {
    private String patientName;

    private String patientEmail;

    private Long patientId;

    private List<AppointmentSerializer> appointmentSerializerList;
}
