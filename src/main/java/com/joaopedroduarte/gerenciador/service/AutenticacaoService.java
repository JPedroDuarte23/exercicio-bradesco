package com.joaopedroduarte.gerenciador.service;

import com.joaopedroduarte.gerenciador.dto.usuario.LoginRequestDTO;
import com.joaopedroduarte.gerenciador.dto.usuario.LoginResponseDTO;
import com.joaopedroduarte.gerenciador.entity.Usuario;
import com.joaopedroduarte.gerenciador.repository.UsuarioRepostory;
import com.joaopedroduarte.gerenciador.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AutenticacaoService {

    @Autowired
    private UsuarioRepostory repostory;

    @Autowired
    private JwtTokenProvider provider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginResponseDTO validarLogin(LoginRequestDTO loginRequest){
        Optional<Usuario> usuario = repostory.findByEmail(loginRequest.getEmail());

        if(usuario.isEmpty()){
            throw new ResponseStatusException(HttpStatusCode.valueOf(401), "E-mail ou senha inválidos.");
        }

        // Verificação de se as senhas conferem

        if(!passwordEncoder.matches(loginRequest.getSenha(), usuario.get().getSenha())){
            throw new ResponseStatusException(HttpStatusCode.valueOf(401), "E-mail ou senha inválidos.");
        }

        // Bloco para geração do token e envio via LoginResponseDTO
        String token = provider.generarTokenForUsuario(usuario.get());
        LoginResponseDTO responseDTO = new LoginResponseDTO();
        responseDTO.setToken(token);

        return responseDTO;
    }

    public void validarToken(String token){
        if(!provider.isTokenValid(token)){
            throw new ResponseStatusException(HttpStatusCode.valueOf(401), "Token inválido");
        }
    }

    public Long buscarIdUsuarioPorToken(String token){
        validarToken(token);
        return provider.getIdByToken(token);
    }
}
