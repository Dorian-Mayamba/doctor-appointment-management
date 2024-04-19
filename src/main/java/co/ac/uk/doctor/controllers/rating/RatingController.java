package co.ac.uk.doctor.controllers.rating;

import co.ac.uk.doctor.requests.ratings.RatingRequest;
import co.ac.uk.doctor.responses.ratings.CreateRatingResponse;
import co.ac.uk.doctor.responses.ratings.DeleteRatingResponse;
import co.ac.uk.doctor.responses.ratings.RatingResponse;
import co.ac.uk.doctor.responses.ratings.UpdateRatingResponse;
import co.ac.uk.doctor.services.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;
    @GetMapping("/ratings/{doctorId}")
    public ResponseEntity<RatingResponse> getRating(@PathVariable("doctorId") Long doctorId){
        return ratingService.getRatings(doctorId);
    }

    @PostMapping("/ratings/create/{doctorId}/{patientId}")
    public ResponseEntity<CreateRatingResponse> createRating(@PathVariable("doctorId") Long doctorId, @PathVariable("patientId") Long patientId,  @RequestBody RatingRequest ratingRequest){
        return ratingService.createRating(doctorId,patientId, ratingRequest);
    }

    @PutMapping("/ratings/update/{ratingId}")
    public ResponseEntity<UpdateRatingResponse> updateRating(@PathVariable("ratingId") Long ratingId,  @RequestBody RatingRequest updateRatingRequest){
        return ratingService.updateRating(ratingId, updateRatingRequest);
    }
    @DeleteMapping("/ratings/delete/{ratingId}")
    public ResponseEntity<DeleteRatingResponse> deleteRating(@PathVariable("ratingId") Long ratingId){
        return ratingService.deleteRating(ratingId);
    }

}
