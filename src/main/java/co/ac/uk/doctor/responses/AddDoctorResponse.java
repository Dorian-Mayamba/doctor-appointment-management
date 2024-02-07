package co.ac.uk.doctor.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class AddDoctorResponse {
    private String message;

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", message);
        return jsonObject.toString(jsonObject.length());
    }
}
