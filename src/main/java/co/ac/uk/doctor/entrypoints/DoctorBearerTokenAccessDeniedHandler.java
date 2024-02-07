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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

public class DoctorBearerTokenAccessDeniedHandler implements AccessDeniedHandler {

    public static final Logger logger = LoggerFactory.getLogger(DoctorBearerTokenAccessDeniedHandler.class);

    private String realmName;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        logger.error(accessDeniedException.getLocalizedMessage(),accessDeniedException);

        Map<String,String> parameters = new LinkedHashMap<>();
        String errorMessage = accessDeniedException.getLocalizedMessage();

        if(Objects.nonNull(realmName)){
            parameters.put("realm", realmName);
        }

        if (request.getUserPrincipal() instanceof AbstractOAuth2TokenAuthenticationToken){
            errorMessage = "The request requires higher privileges than provided by the access token.";
            parameters.put("error", "insufficient_scope");
            parameters.put("error_description", errorMessage);
            parameters.put("error_uri", "https://tools.ietf.org/html/rfc6750#section-3.1");
        }

        ErrorResponse errorResponse = new ErrorResponse("Unauthorized", errorMessage, request.getRequestURI());
        ResponseEntity<ErrorResponse> responseEntity = ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorResponse);

        String wwwAuthenticate = computeWWWAuthenticateHeaderValue(parameters);
        response.addHeader("WWW-Authenticate", wwwAuthenticate);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(responseEntity.getBody().toString());
    }

    public static String computeWWWAuthenticateHeaderValue(Map<String,String> parameters){
        StringJoiner wwwAuthenticate = new StringJoiner(", ", "Bearer ", "");

        if (!parameters.isEmpty()){
            parameters.forEach((k,v)->wwwAuthenticate.add(k + "=\"" + v +"\""));
        }
        return wwwAuthenticate.toString();
    }
}
