package co.ac.uk.doctor.utils;

import co.ac.uk.doctor.entities.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JWTUtil {
    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000; // 24 hours

    @Value("${app.secret}")
    private String secret;
    @Value("${app.issuer}")
    private String issuer;

    public String generateToken(User user){
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(user.getName())
                    .withClaim("userId", user.getId())
                    .withExpiresAt(new Date(System.currentTimeMillis() * EXPIRE_DURATION))
                    .withClaim("createdAt", new Date())
                    .withIssuedAt(new Date())
                    .sign(algorithm);
    }

    public Claim getClaim(String token, String claimKey){
        return JWT.decode(token).getClaim(claimKey);
    }

    public DecodedJWT verify(String token){
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
        return verifier.verify(token);
    }
}
