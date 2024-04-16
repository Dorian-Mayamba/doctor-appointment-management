package co.ac.uk.doctor.controllers.profile;

import co.ac.uk.doctor.requests.ProfileDTO;
import co.ac.uk.doctor.responses.ProfileResponse;
import co.ac.uk.doctor.services.ProfileService;
import co.ac.uk.doctor.entities.Doctor;
import co.ac.uk.doctor.entities.Patient;
import co.ac.uk.doctor.entities.generic.IUserDetails;
import io.micrometer.common.lang.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
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

    @PutMapping( value= "/profile/update/{email}",  consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ProfileResponse> updateProfile(
            @PathVariable("email") String email,
            @RequestPart("profile") @Nullable MultipartFile file,
            @RequestPart("data") ProfileDTO profileDTO) {
        try {
            if(Objects.nonNull(file)){
                profileDTO.setProfile(file);
            }
            IUserDetails iUserDetails = profileService.updateProfile(email, profileDTO);
            log.info("User: " + iUserDetails.getName() + " profile: " + iUserDetails.getUserProfile());
            JSONObject userData = new JSONObject(profileDTO);
            return ResponseEntity
                    .ok(ProfileResponse.builder()
                            .message("Your profile has been updated")
                            .profile(iUserDetails.getUserProfile())
                            .userData(
                                    userData.toString(userData.length())
                            )
                            .build());
        } catch (IOException ex) {
            log.error("Error: " + ex.getLocalizedMessage(), ex);
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(ProfileResponse.builder().message("Failed to update your profile").build());
        }
    }

    @GetMapping("/profile/{email}")
    public ResponseEntity<ProfileDTO> getUserProfile(@PathVariable("email") String email) {
        IUserDetails userDetails = profileService.getUserProfile(email);
        if (userDetails instanceof Doctor) {
            Doctor d = (Doctor) userDetails;
            return ResponseEntity.
                    ok()
                    .body(ProfileDTO
                            .builder()
                            .username(d.getName())
                            .email(d.getDoctorEmail())
                            .number(d.getNumber()).profilePath(userDetails.getUserProfile()).build());

        } else {
            Patient p = (Patient) userDetails;
            return ResponseEntity
                    .ok()
                    .body(ProfileDTO
                            .builder()
                            .username(p.getPatientName())
                            .number(p.getNumber())
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
