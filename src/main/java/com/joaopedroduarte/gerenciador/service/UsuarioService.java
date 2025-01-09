package com.joaopedroduarte.gerenciador.service;

import com.joaopedroduarte.gerenciador.entity.Usuario;
import com.joaopedroduarte.gerenciador.repository.UsuarioRepostory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepostory repostory;

    @Autowired
    private AutenticacaoService autenticacaoService;

    public Usuario getUsuarioByToken(String token){
        Long idUsuario = autenticacaoService.getIdByToken(token);
        Optional<Usuario> usuario = repostory.findById(idUsuario);

        if(usuario.isEmpty()){
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "O usuário informado não existe");
        }

        return usuario.get();
    }

    public Usuario getUsuarioById(Long idUsuario){
        Optional<Usuario> usuario = repostory.findById(idUsuario);
        if(usuario.isEmpty()){
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Usuário não encontrado");
        }

        return usuario.get();
    }

}
