package co.ac.uk.doctor.entrypoints;

import co.ac.uk.doctor.responses.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

public class DoctorAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(DoctorAuthenticationEntryPoint.class);

    private String realmName;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        logger.error("Error: "+ authException.getLocalizedMessage(), authException);
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String errorMessage = "Incorrect email or password";
        Map<String, String> parameters = new LinkedHashMap<>();

        if (Objects.nonNull(realmName)){
            parameters.put("realmName", realmName);
        }

        if (authException instanceof OAuth2AuthenticationException){
            OAuth2Error error = ((OAuth2AuthenticationException)authException).getError();
            parameters.put("error", error.getErrorCode());

            if (StringUtils.hasText(error.getDescription())) {
                errorMessage = error.getDescription();
                parameters.put("error_description", errorMessage);
            }
            if (StringUtils.hasText(error.getUri())) {
                parameters.put("error_uri", error.getUri());
            }

            if (error instanceof BearerTokenError){
                BearerTokenError bearerTokenError = (BearerTokenError) error;

                if (StringUtils.hasText(bearerTokenError.getScope())){
                    parameters.put("scope", bearerTokenError.getScope());
                }

                status = bearerTokenError.getHttpStatus();
            }
        }

        ResponseEntity<ErrorResponse> responseEntity = ResponseEntity
                .status(status)
                .body(new ErrorResponse("Unauthenticated", errorMessage, request.getRequestURI()));
        String wwwAuthenticateHeaderValue = computeWWWAuthenticateHeaderValue(parameters);
        response.addHeader("WWW-Authenticate", wwwAuthenticateHeaderValue);
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(responseEntity.getBody().toString());

    }

    private void setRealmName(String realmName){
        this.realmName = realmName;
    }

    public static String computeWWWAuthenticateHeaderValue(Map<String, String> parameters) {
        StringJoiner wwwAuthenticate = new StringJoiner(", ", "Bearer ", "");
        if (!parameters.isEmpty()) {
            parameters.forEach((k, v) -> wwwAuthenticate.add(k + "=\"" + v + "\""));
        }
        return wwwAuthenticate.toString();
    }
}
