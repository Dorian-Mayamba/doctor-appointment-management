package co.ac.uk.doctor.controllers.rating;

import co.ac.uk.doctor.services.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RatingController {
    @Autowired
    private RatingService ratingService;
}
