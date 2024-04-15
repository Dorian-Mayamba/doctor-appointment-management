package co.ac.uk.doctor.services.generic;

import co.ac.uk.doctor.userdetails.generic.IUserDetails;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileStorageService {


    void save(MultipartFile file, String filename, IUserDetails userDetails) throws IOException;

    Resource load(String filename);

    void deleteAll();

    Stream<Path> loadAll();

}
