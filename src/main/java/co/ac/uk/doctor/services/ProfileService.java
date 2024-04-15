package co.ac.uk.doctor.services;

import co.ac.uk.doctor.userdetails.Doctor;
import co.ac.uk.doctor.userdetails.Patient;
import co.ac.uk.doctor.userdetails.generic.IUserDetails;
import co.ac.uk.doctor.repositories.DoctorRepository;
import co.ac.uk.doctor.repositories.PatientRepository;
import co.ac.uk.doctor.requests.ProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private FileStorageServiceImpl fileStorageService;

    public IUserDetails updateProfile(Long userId, ProfileDTO profileDTO) throws IOException {
        IUserDetails iUserDetails = getUserProfile(userId);
        if(Objects.nonNull(profileDTO.getProfile())){
            MultipartFile file = profileDTO.getProfile();
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            fileStorageService.save(file,filename,iUserDetails);
        }
        if(iUserDetails instanceof Doctor){
            Doctor d = (Doctor) iUserDetails;
            d.setDoctorEmail(profileDTO.getEmail());
            d.setDoctorName(profileDTO.getUsername());
            d.setDoctorNumber(profileDTO.getNumber());
            doctorRepository.save(d);
        }else if(iUserDetails instanceof Patient){
            Patient p = (Patient) iUserDetails;
            p.setPatientName(profileDTO.getUsername());
            p.setPatientEmail(profileDTO.getEmail());
            p.setPatientNumber(profileDTO.getNumber());
            patientRepository.save(p);
        }

        return iUserDetails;
    }

    public IUserDetails getUserProfile(Long userId){
        return getPatientOrDoctor(userId);
    }

    private IUserDetails getPatientOrDoctor(Long userId){
        Optional<Doctor> doctorOptional = doctorRepository.findById(userId);
        Optional<Patient> patientOptional = patientRepository.findById(userId);
        IUserDetails iUserDetails = null;
        if(doctorOptional.isPresent()){
            iUserDetails = doctorOptional.get();
        }else if(patientOptional.isPresent()){
            iUserDetails = patientOptional.get();
        }
        return iUserDetails;
    }

    public Resource loadRessource(String filename) {
        return fileStorageService.load(filename);
    }
}
