package com.joaopedroduarte.gerenciador.service;

import com.joaopedroduarte.gerenciador.repository.UsuarioRepostory;
import com.joaopedroduarte.gerenciador.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AutenticacaoService {

    @Autowired
    private UsuarioRepostory repostory;

    private JwtTokenProvider provider;

    public void validarToken(String token){
        if(!provider.isTokenValid(token)){
            throw new ResponseStatusException(HttpStatusCode.valueOf(401), "Token inválido");
        }
    }

    public Long getIdByToken(String token){
        validarToken(token);
        return provider.getIdByToken(token);
    }
}
