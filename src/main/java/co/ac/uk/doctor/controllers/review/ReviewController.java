package co.ac.uk.doctor.controllers.review;

import co.ac.uk.doctor.requests.reviews.ReviewRequest;
import co.ac.uk.doctor.responses.reviews.ReviewResponse;
import co.ac.uk.doctor.serializers.ReviewSerializer;
import co.ac.uk.doctor.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/reviews/{doctorId}")
    public ResponseEntity<List<ReviewSerializer>> getReviews(@PathVariable("doctorId") Long doctorId) {
        return reviewService.getReviews(doctorId);
    }

    @PostMapping("/reviews/create/{doctorId}/{patientId}")
    public ResponseEntity<ReviewResponse> createReview(@PathVariable("doctorId") Long doctorId, @PathVariable("patientId") Long patientId, @RequestBody ReviewRequest reviewRequest) {
        return reviewService.createReview(doctorId, patientId, reviewRequest);
    }

    @PutMapping("/reviews/update/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable("reviewId") Long reviewId, @RequestBody ReviewRequest reviewRequest){
        return reviewService.updateReview(reviewId, reviewRequest);
    }

    @DeleteMapping("/reviews/delete/{reviewId}")
    public ResponseEntity<ReviewResponse> deleteReview(@PathVariable("reviewId") Long reviewId){
        return reviewService.deleteReview(reviewId);
    }

}
