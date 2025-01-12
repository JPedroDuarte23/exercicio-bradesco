package com.joaopedroduarte.gerenciador.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.joaopedroduarte.gerenciador.entity.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class JwtTokenProvider {

    @Value("${jwt.palavraPasse}")
    private String palavraPasseToken;

    @Value("${jwt.validade}")
    private long tempoDeExpiracao;
    public String generarTokenForUsuario(Usuario usuario) {

        return JWT.create()
                .withClaim("id", usuario.getId())
                .withExpiresAt(new Date(System.currentTimeMillis() + tempoDeExpiracao))
                .sign(Algorithm.HMAC256(palavraPasseToken));
    }
    private DecodedJWT decodificarJWT(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(palavraPasseToken)).build();
        return verifier.verify(token);
    }

    public Long getIdByToken(String token) {
        DecodedJWT decodedJWT = decodificarJWT(token);
        return decodedJWT.getClaim("id").asLong();
    }

    public boolean isTokenValid(String token) {
        try {
            decodificarJWT(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }



}
