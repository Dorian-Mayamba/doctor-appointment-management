package co.ac.uk.doctor.utils;

import co.ac.uk.doctor.entities.*;
import co.ac.uk.doctor.serializers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
                .averageRating(getAvgRating(doctor.getRatings()))
                .ratings(toRatingSerializer(doctor.getRatings()))
                .reviews(toReviewSerializer(doctor.getReviews()))
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

    public static AppointmentSerializer toAppointmentSerializer(Appointment appointment){
        return AppointmentSerializer
                .builder().
                title(appointment.getTitle())
                .startTime(appointment.getTime())
                .endTime(appointment.getEndTime())
                .date(appointment.getDate())
                .patientName(appointment.getPatient().getPatientName())
                .doctorName(appointment.getDoctor().getDoctorName())
                .build();
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

    public static List<RatingSerializer> toRatingSerializer(List<Rating> ratings) {
        return
                ratings.stream()
                        .map((rating -> RatingSerializer.builder()
                                .patientPicture(rating.getPatient().getUserProfile())
                                .patientName(rating.getPatient().getPatientName())
                                .build()))
                        .collect(Collectors.toList());
    }

    public static List<ReviewSerializer> toReviewSerializer(List<Review>  reviews){
        return
                reviews.stream()
                        .map((review -> ReviewSerializer.builder()
                                .content(review.getContent())
                                .patientName(review.getPatient().getName())
                                .patientPicture(review.getPatient().getUserProfile())
                                .build()))
                        .collect(Collectors.toList());
    }

    public static ReviewSerializer toReviewSerializer(Review review){
        return ReviewSerializer
                .builder()
                .content(review.getContent())
                .patientName(review.getPatient().getName())
                .patientPicture(review.getPatient().getUserProfile())
                .build();
    }

    public static RatingSerializer toRatingSerializer(Rating rating){
        return RatingSerializer
                .builder()
                .rating(rating.getRating())
                .patientName(rating.getPatient().getName())
                .patientPicture(rating.getPatient().getUserProfile())
                .build();
    }

    private static Double getAvgRating(List<Rating> ratings){
        Double avgRating = 0.0;
        if (ratings.size() == 0){
            return avgRating;
        }
        for (Rating rating:ratings){
            avgRating+=rating.getRating();
        }
        return avgRating / ratings.size();
    }
}
