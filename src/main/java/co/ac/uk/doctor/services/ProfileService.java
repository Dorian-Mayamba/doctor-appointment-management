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
        JSONObject data = new JSONObject();
        if (Objects.nonNull(profileDTO.getProfile())){
            userDetails.setProfile(profileDTO.getProfile().getOriginalFilename());
            fileStorageService.save(profileDTO.getProfile(), userDetails.getProfile());
        }
        userDetails.setEmail(profileDTO.getEmail());
        userDetails.setName(profileDTO.getUsername());
        userDetails.setNumber(profileDTO.getNumber());
        if (userDetails instanceof Doctor){
            Doctor d = (Doctor) userDetails;
            doctorService.saveDoctor(d);
            UpdateProfileResponse profileResponse =
                    UpdateProfileResponse
                            .builder()
                            .profile(d.getProfile())
                            .username(d.getName())
                            .number(d.getNumber())
                            .email(d.getEmail())
                            .message("Your profile has been updated")
                            .build();
            return ResponseEntity
                    .ok()
                    .body(profileResponse);

        }else{
            Patient p = (Patient) userDetails;
            patientService.savePatient(p);
            UpdateProfileResponse profileResponse =
                    UpdateProfileResponse
                            .builder()
                            .profile(p.getProfile())
                            .message("Your profile has been updated")
                            .username(p.getName())
                            .email(p.getEmail())
                            .number(p.getNumber())
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
