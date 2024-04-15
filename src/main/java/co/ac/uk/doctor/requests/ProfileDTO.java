package co.ac.uk.doctor.requests;

import io.micrometer.common.lang.Nullable;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDTO {
    private String username;
    private String email;
    private String number;
    private @Nullable MultipartFile profile;
    private String profilePath;
}
