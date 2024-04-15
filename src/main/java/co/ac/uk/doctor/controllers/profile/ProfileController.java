package co.ac.uk.doctor.controllers.profile;

import co.ac.uk.doctor.requests.ProfileDTO;
import co.ac.uk.doctor.responses.ProfileResponse;
import co.ac.uk.doctor.services.ProfileService;
import co.ac.uk.doctor.userdetails.Doctor;
import co.ac.uk.doctor.userdetails.Patient;
import co.ac.uk.doctor.userdetails.generic.IUserDetails;
import io.micrometer.common.lang.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
@Slf4j
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping( value= "/profile/update/{userId}",  consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ProfileResponse> updateProfile(
            @PathVariable("userId") Long userId,
            @RequestPart("profile") @Nullable MultipartFile file,
            @RequestPart("data") ProfileDTO profileDTO) {
        try {
            if(Objects.nonNull(file)){
                profileDTO.setProfile(file);
            }
            IUserDetails iUserDetails = profileService.updateProfile(userId, profileDTO);
            log.info("User: " + iUserDetails.getName() + " profile: " + iUserDetails.getUserProfile());
            return ResponseEntity
                    .ok(ProfileResponse.builder()
                            .message("Your profile has been updated")
                            .profile(iUserDetails.getUserProfile())
                            .build());
        } catch (IOException ex) {
            log.error("Error: " + ex.getLocalizedMessage(), ex);
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(ProfileResponse.builder().message("Failed to update your profile").build());
        }
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<ProfileDTO> getUserProfile(@PathVariable("userId") Long userId) {
        IUserDetails userDetails = profileService.getUserProfile(userId);
        if (userDetails instanceof Doctor) {
            Doctor d = (Doctor) userDetails;
            return ResponseEntity.
                    ok()
                    .body(ProfileDTO
                            .builder()
                            .username(d.getName())
                            .email(d.getDoctorEmail())
                            .number(d.getDoctorNumber()).profilePath(userDetails.getUserProfile()).build());

        } else {
            Patient p = (Patient) userDetails;
            return ResponseEntity
                    .ok()
                    .body(ProfileDTO
                            .builder()
                            .username(p.getPatientName())
                            .number(p.getPatientNumber())
                            .email(p.getPatientEmail()).profilePath(userDetails.getUserProfile()).build());

        }
    }

    @GetMapping("/profile/uploads/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable("filename") String filename){
        try{
            Resource file = profileService.loadRessource(filename);
            return ResponseEntity
                    .ok(file);
        }catch (Exception ex){
            throw new RuntimeException("could not load file");
        }
    }

}
