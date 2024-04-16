package co.ac.uk.doctor.controllers.review;

import co.ac.uk.doctor.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
}
