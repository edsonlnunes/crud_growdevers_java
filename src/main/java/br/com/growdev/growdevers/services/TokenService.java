package br.com.growdev.growdevers.services;

import br.com.growdev.growdevers.models.Growdever;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private String secret = "Growdev@2020";

    public String getToken(Growdever growdever){
        var algorithm = Algorithm.HMAC256(secret);
        var expiresDate = LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
        return JWT.create()
                .withIssuer("API Crud Growdevers")
                .withSubject(growdever.getEmail())
                .withClaim("id", growdever.getId().toString())
                .withExpiresAt(expiresDate)
                .sign(algorithm);
    }

    public String verifyToken(String token){
        var algorithm = Algorithm.HMAC256(secret);
        return JWT.require(algorithm)
                .withIssuer("API Crud Growdevers")
                .build()
                .verify(token)
                .getSubject();

    }
}
