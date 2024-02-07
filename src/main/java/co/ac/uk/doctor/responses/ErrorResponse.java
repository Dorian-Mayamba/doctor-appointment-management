package co.ac.uk.doctor.responses;

import lombok.*;
import org.json.JSONObject;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String error;
    private String message;
    private String path;

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error", error);
        jsonObject.put("message", message);
        jsonObject.put("path", path);
        return jsonObject.toString(jsonObject.length());
    }

}
