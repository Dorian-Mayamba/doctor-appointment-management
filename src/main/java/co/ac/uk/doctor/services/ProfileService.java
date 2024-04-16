package co.ac.uk.doctor.services;

import co.ac.uk.doctor.entities.Doctor;
import co.ac.uk.doctor.entities.Patient;
import co.ac.uk.doctor.entities.generic.IUserDetails;
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

    public IUserDetails updateProfile(String email, ProfileDTO profileDTO) throws IOException {
        IUserDetails iUserDetails = getUserProfile(email);
        if(Objects.nonNull(profileDTO.getProfile())){
            MultipartFile file = profileDTO.getProfile();
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            fileStorageService.save(file,filename,iUserDetails);
        }
        if(iUserDetails instanceof Doctor){
            Doctor d = (Doctor) iUserDetails;
            d.setDoctorEmail(profileDTO.getEmail());
            d.setDoctorName(profileDTO.getUsername());
            d.setNumber(profileDTO.getNumber());
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

    public IUserDetails getUserProfile(String email){
        return getPatientOrDoctor(email);
    }

    private IUserDetails getPatientOrDoctor(String email){
        Optional<Doctor> doctorOptional = doctorRepository.getDoctorByDoctorEmail(email);
        Optional<Patient> patientOptional = patientRepository.getPatientByPatientEmail(email);
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
