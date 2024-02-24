package co.ac.uk.doctor.serializers;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DoctorSerializer {
    private String doctorName;

    private String doctorEmail;

    private Long doctorId;

    private String doctorSpeciality;

    private List<AppointmentSerializer> appointmentSerializers;
}
