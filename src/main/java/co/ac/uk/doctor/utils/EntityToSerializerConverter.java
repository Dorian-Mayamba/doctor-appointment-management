package co.ac.uk.doctor.utils;

import co.ac.uk.doctor.entities.*;
import co.ac.uk.doctor.serializers.*;

import java.util.List;
import java.util.stream.Collectors;

public class EntityToSerializerConverter {

    public static DoctorSerializer toDoctorSerializer(Doctor doctor) {
        return DoctorSerializer
                .builder()
                .doctorId(doctor.getId())
                .doctorEmail(doctor.getEmail())
                .doctorName(doctor.getName())
                .doctorSpeciality(doctor.getSpeciality())
                .doctorNumber(doctor.getNumber())
                .doctorProfile(doctor.getProfile())
                .appointments(toAppointmentsSerializer(doctor.getAppointments()))
                .reviews(toReviewSerializer(doctor.getReviews()))
                .averageRating(getAvgRating(doctor.getReviews()))
                .build();
    }

    public static List<DoctorSerializer> toDoctorSerializer(List<Doctor> doctors){
        return doctors.stream()
                .map(EntityToSerializerConverter::toDoctorSerializer)
                .collect(Collectors.toList());
    }

    public static SlotSerializer toSlotSerializer(Slot slot){
        return SlotSerializer
                .builder()
                .id(slot.getId())
                .time(slot.getTime())
                .date(slot.getDate())
                .month(slot.getMonth())
                .isBooked(slot.isBooked()).build();
    }

    public static List<SlotSerializer> toSlotSerializer(List<Slot> slots){
        return slots.stream()
                .map(EntityToSerializerConverter::toSlotSerializer)
                .collect(Collectors.toList());
    }

    public static List<AppointmentSerializer> toAppointmentsSerializer(List<Appointment> appointments) {
        return appointments
                .stream().map(EntityToSerializerConverter::toAppointmentSerializer)
                .collect(Collectors.toList());
    }

    public static AppointmentSerializer toAppointmentSerializer(Appointment appointment){
        return AppointmentSerializer
                .builder()
                .title(appointment.getTitle())
                .startTime(appointment.getTime())
                .endTime(appointment.getEndTime())
                .date(appointment.getDate())
                .patientName(appointment.getPatient().getName())
                .doctorName(appointment.getDoctor().getName())
                .patientEmail(appointment.getPatient().getEmail())
                .doctorEmail(appointment.getDoctor().getEmail())
                .doctorPicture(appointment.getDoctor().getProfile())
                .patientPicture(appointment.getPatient().getProfile())
                .status(appointment.getStatus())
                .id(appointment.getId())
                .build();
    }

    public static PatientSerializer toPatientSerializer(Patient patient) {
        return PatientSerializer
                .builder()
                .patientId(patient.getId())
                .patientName(patient.getName())
                .patientEmail(patient.getEmail())
                .patientNumber(patient.getNumber())
                .patientProfile(patient.getProfile())
                .patientId(patient.getId())
                .appointments(toAppointmentsSerializer(patient.getAppointments()))
                .reviews(toReviewSerializer(patient.getReviews()))
                .build();
    }

    public static List<PatientSerializer> toPatientSerializer(List<Patient> patients){
        return patients
                .stream().map(EntityToSerializerConverter::toPatientSerializer)
                .collect(Collectors.toList());
    }

    public static List<RatingSerializer> toRatingSerializer(List<Rating> ratings) {
        return
                ratings.stream()
                        .map((rating -> RatingSerializer.builder()
                                .patientPicture(rating.getPatient().getProfile())
                                .patientName(rating.getPatient().getName())
                                .build()))
                        .collect(Collectors.toList());
    }

    public static List<ReviewSerializer> toReviewSerializer(List<Review>  reviews){
        return
                reviews.stream()
                        .map(EntityToSerializerConverter::toReviewSerializer)
                        .collect(Collectors.toList());
    }

    public static ReviewSerializer toReviewSerializer(Review review){
        return ReviewSerializer
                .builder()
                .content(review.getContent())
                .rating(review.getRating())
                .patientName(review.getPatient().getName())
                .patientPicture(review.getPatient().getProfile())
                .build();
    }

    public static RatingSerializer toRatingSerializer(Rating rating){
        return RatingSerializer
                .builder()
                .rating(rating.getRating())
                .patientName(rating.getPatient().getName())
                .patientPicture(rating.getPatient().getProfile())
                .build();
    }

    private static Double getAvgRating(List<Review> reviews){
        Double avgRating = 0.0;
        if (reviews.size() == 0){
            return avgRating;
        }
        for (Review review:reviews){
            avgRating+=review.getRating();
        }
        return avgRating / reviews.size();
    }
}
