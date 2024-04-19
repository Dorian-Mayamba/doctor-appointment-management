package co.ac.uk.doctor.services;

import co.ac.uk.doctor.entities.Doctor;
import co.ac.uk.doctor.entities.Patient;
import co.ac.uk.doctor.entities.generic.IUserDetails;
import co.ac.uk.doctor.requests.ProfileDTO;
import co.ac.uk.doctor.responses.DeleteProfileResponse;
import co.ac.uk.doctor.responses.UpdateProfileResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Objects;

@Service
public class ProfileService {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private FileStorageServiceImpl fileStorageService;

    public ResponseEntity<UpdateProfileResponse> updateProfile(String email, ProfileDTO profileDTO) throws IOException {

        IUserDetails userDetails = getUserProfile(email);
        JSONObject data = new JSONObject(profileDTO);
        if (Objects.nonNull(profileDTO.getProfile())){
            userDetails.setUserProfile(profileDTO.getProfile().getOriginalFilename());
        }
        if (userDetails instanceof Doctor){
            Doctor d = (Doctor) userDetails;
            UpdateProfileResponse profileResponse =
                    UpdateProfileResponse
                            .builder()
                            .profile(d.getUserProfile())
                            .message("Your profile has been updated")
                            .userData(data.toString(data.length()))
                            .build();
            return ResponseEntity
                    .ok()
                    .body(profileResponse);

        }else{
            Patient p = (Patient) userDetails;
            UpdateProfileResponse profileResponse =
                    UpdateProfileResponse
                            .builder()
                            .profile(p.getUserProfile())
                            .message("Your profile has been updated")
                            .userData(data.toString(data.length()))
                            .build();
            return ResponseEntity
                    .ok()
                    .body(profileResponse);
        }
    }

    public IUserDetails getUserProfile(String email){
        return getPatientOrDoctor(email);
    }

    private IUserDetails getPatientOrDoctor(String email){
        Doctor doctor = doctorService.findByEmail(email);
        Patient patient = patientService.findByEmail(email);
        if (doctor != null){
            return doctor;
        }
        return patient;
    }

    public Resource loadRessource(String filename) {
        return fileStorageService.load(filename);
    }

    public ResponseEntity<DeleteProfileResponse> deleteProfile(String email) {
        IUserDetails userDetails = getUserProfile(email);
        if (userDetails instanceof Doctor){
            Doctor d = (Doctor) userDetails;
            doctorService.deleteUser(d);
        }else{
            Patient p = (Patient) userDetails;
            patientService.deleteUser(p);
        }
        return ResponseEntity
                .ok()
                .body(DeleteProfileResponse
                        .builder()
                        .message("Your profile has been deleted")
                        .build());
    }
}
