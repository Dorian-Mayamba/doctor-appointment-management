package co.ac.uk.doctor.utils;

import co.ac.uk.doctor.entities.Appointment;
import co.ac.uk.doctor.entities.Doctor;
import co.ac.uk.doctor.entities.Patient;
import co.ac.uk.doctor.serializers.AppointmentSerializer;
import co.ac.uk.doctor.serializers.DoctorSerializer;
import co.ac.uk.doctor.serializers.PatientSerializer;

import java.util.ArrayList;
import java.util.List;

public class EntityToSerializerConverter {

    public static DoctorSerializer toDoctorSerializer(Doctor doctor) {
        return DoctorSerializer
                .builder()
                .doctorId(doctor.getId())
                .doctorEmail(doctor.getDoctorEmail())
                .doctorName(doctor.getDoctorName())
                .doctorSpeciality(doctor.getSpeciality())
                .doctorNumber(doctor.getNumber())
                .doctorProfile(doctor.getUserProfile())
                .appointments(toAppointmentsSerializer(doctor.getDoctorAppointments()))
                .build();
    }

    public static List<AppointmentSerializer> toAppointmentsSerializer(List<Appointment> appointments) {
        List<AppointmentSerializer> appointmentSerializers = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentSerializers.add(AppointmentSerializer
                    .builder().
                    title(appointment.getTitle())
                    .startTime(appointment.getTime())
                    .endTime(appointment.getEndTime())
                    .date(appointment.getDate())
                    .patientName(appointment.getPatient().getPatientName())
                    .doctorName(appointment.getDoctor().getDoctorName())
                    .build());
        }
        return appointmentSerializers;
    }

    public static PatientSerializer toPatientSerializer(Patient patient) {
        return PatientSerializer
                .builder()
                .patientId(patient.getId())
                .patientName(patient.getPatientName())
                .patientEmail(patient.getPatientEmail())
                .patientNumber(patient.getNumber())
                .patientProfile(patient.getUserProfile())
                .patientId(patient.getId())
                .appointmentSerializerList(toAppointmentsSerializer(patient.getPatientAppointments()))
                .build();
    }
}
