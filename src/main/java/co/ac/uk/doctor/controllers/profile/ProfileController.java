package co.ac.uk.doctor.controllers.profile;

import co.ac.uk.doctor.requests.ProfileDTO;
import co.ac.uk.doctor.responses.DeleteProfileResponse;
import co.ac.uk.doctor.responses.ProfileResponse;
import co.ac.uk.doctor.responses.UpdateProfileResponse;
import co.ac.uk.doctor.services.ProfileService;
import co.ac.uk.doctor.entities.Doctor;
import co.ac.uk.doctor.entities.Patient;
import co.ac.uk.doctor.entities.generic.IUserDetails;
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

    @PutMapping( value= "/profile/update/{email}",  consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UpdateProfileResponse> updateProfile(
            @PathVariable("email") String email,
            @RequestPart("profile") @Nullable MultipartFile file,
            @RequestPart("data") ProfileDTO profileDTO) {
        try {
            if(Objects.nonNull(file)){
                profileDTO.setProfile(file);
            }
            return profileService.updateProfile(email,profileDTO);
        } catch (IOException ex) {
            log.error("Error: " + ex.getLocalizedMessage(), ex);
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(UpdateProfileResponse.builder().message("Failed to update your profile").build());
        }
    }

    @GetMapping("/profile/{email}")
    public ResponseEntity<ProfileResponse> getUserProfile(@PathVariable("email") String email) {
        IUserDetails userDetails = profileService.getUserProfile(email);
        if (userDetails instanceof Doctor) {
            Doctor d = (Doctor) userDetails;
            return ResponseEntity.
                    ok()
                    .body(ProfileResponse
                            .builder()
                            .username(d.getName())
                            .email(d.getEmail())
                            .number(d.getNumber()).profile(userDetails.getProfile()).build());

        } else {
            Patient p = (Patient) userDetails;
            return ResponseEntity
                    .ok()
                    .body(ProfileResponse
                            .builder()
                            .username(p.getName())
                            .number(p.getNumber())
                            .email(p.getEmail()).profile(userDetails.getProfile()).build());

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

    @DeleteMapping("/profile/delete/{email}")
    public ResponseEntity<DeleteProfileResponse> deleteProfile(@PathVariable("email")String email){
        return profileService.deleteProfile(email);
    }

}
