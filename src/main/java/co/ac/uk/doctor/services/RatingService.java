package co.ac.uk.doctor.services;

import co.ac.uk.doctor.entities.Doctor;
import co.ac.uk.doctor.entities.Patient;
import co.ac.uk.doctor.entities.Rating;
import co.ac.uk.doctor.repositories.RatingRepository;
import co.ac.uk.doctor.requests.ratings.RatingRequest;
import co.ac.uk.doctor.responses.ratings.CreateRatingResponse;
import co.ac.uk.doctor.responses.ratings.DeleteRatingResponse;
import co.ac.uk.doctor.responses.ratings.RatingResponse;
import co.ac.uk.doctor.responses.ratings.UpdateRatingResponse;
import co.ac.uk.doctor.serializers.RatingSerializer;
import co.ac.uk.doctor.utils.EntityToSerializerConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;

    private final DoctorService doctorService;

    private final PatientService patientService;


    public ResponseEntity<DeleteRatingResponse> deleteRating(Long ratingId) {
        ratingRepository.deleteById(ratingId);
        return ResponseEntity
                .ok(new DeleteRatingResponse("Successfully removed your rating"));
    }

    public ResponseEntity<UpdateRatingResponse> updateRating(Long ratingId, RatingRequest updateRatingRequest) {
        Rating rating = ratingRepository.findById(ratingId).orElse(null);
        assert rating != null : "Rating should not be null";
        rating.setRating(updateRatingRequest.getRating());
        return ResponseEntity
                .ok()
                .body(UpdateRatingResponse.builder()
                        .message("Successfully updated your rating")
                        .ratingData(EntityToSerializerConverter.toRatingSerializer(rating))
                        .build());
    }

    public ResponseEntity<CreateRatingResponse> createRating(Long doctorId, Long patientId, RatingRequest ratingRequest) {
        Doctor doctor = (Doctor) doctorService.loadUserById(doctorId);
        Patient patient = (Patient) patientService.loadUserById(patientId);
        Rating rating = Rating.builder()
                .rating(ratingRequest.getRating())
                .doctor(doctor)
                .patient(patient)
                .build();
        ratingRepository.save(rating);
        return ResponseEntity
                .ok()
                .body(CreateRatingResponse
                        .builder()
                        .ratingData(EntityToSerializerConverter.toRatingSerializer(rating))
                        .message("Successfully created your rating")
                        .build());
    }

    public ResponseEntity<RatingResponse> getRatings(Long doctorId) {
        Doctor doctor = (Doctor) doctorService.loadUserById(doctorId);
        Double avgRating = getAvgRating(doctor.getRatings());
        List<RatingSerializer> ratings = EntityToSerializerConverter.toRatingSerializer(doctor.getRatings());
        return ResponseEntity
                .ok()
                .body(RatingResponse
                        .builder()
                        .averageRating(avgRating)
                        .ratings(ratings)
                        .build());
    }

    private Double getAvgRating(List<Rating> ratings){
        Double avgRating = 0.0;
        for (Rating rating:ratings){
            avgRating+=rating.getRating();
        }
        return avgRating / ratings.size();
    }


}
