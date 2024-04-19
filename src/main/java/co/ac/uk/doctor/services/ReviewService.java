package co.ac.uk.doctor.services;

import co.ac.uk.doctor.entities.Doctor;
import co.ac.uk.doctor.entities.Patient;
import co.ac.uk.doctor.entities.Review;
import co.ac.uk.doctor.repositories.ReviewRepository;
import co.ac.uk.doctor.requests.reviews.ReviewRequest;
import co.ac.uk.doctor.responses.reviews.ReviewResponse;
import co.ac.uk.doctor.serializers.ReviewSerializer;
import co.ac.uk.doctor.utils.EntityToSerializerConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final PatientService patientService;

    private final DoctorService doctorService;



    public ResponseEntity<List<ReviewSerializer>> getReviews(Long doctorId){
        Doctor doctor = (Doctor) doctorService.loadUserById(doctorId);
        return ResponseEntity
                .ok()
                .body(EntityToSerializerConverter.toReviewSerializer(doctor.getReviews()));
    }
    public ResponseEntity<ReviewResponse> createReview(Long doctorId, Long patientId, ReviewRequest reviewRequest){
        Doctor doctor = (Doctor) doctorService.loadUserById(doctorId);
        Patient patient = (Patient) patientService.loadUserById(patientId);
        Review review = Review.builder()
                .content(reviewRequest.getContent())
                .patient(patient)
                .doctor(doctor)
                .build();
        reviewRepository.save(review);
        return ResponseEntity
                .ok()
                .body(ReviewResponse
                        .builder()
                        .reviewSerializer(EntityToSerializerConverter.toReviewSerializer(review))
                        .message("Your review has been submitted")
                        .build());
    }

    public ResponseEntity<ReviewResponse> updateReview(Long reviewId, ReviewRequest reviewRequest){
        Review review = reviewRepository.findById(reviewId).orElse(null);
        assert review != null : "Review cannot be null";
        return ResponseEntity.ok()
                .body(ReviewResponse
                        .builder()
                        .reviewSerializer(EntityToSerializerConverter.toReviewSerializer(review))
                        .message("Your review has been updated")
                        .build());
    }

    public ResponseEntity<ReviewResponse> deleteReview(Long reviewId){
        reviewRepository.deleteById(reviewId);
        return ResponseEntity
                .ok()
                .body(ReviewResponse.builder()
                        .message("Your review has been deleted").build());
    }
}
