package com.joaopedroduarte.gerenciador.service;

import com.joaopedroduarte.gerenciador.mapper.UsuarioMapper;
import com.joaopedroduarte.gerenciador.dto.usuario.UsuarioCreateDTO;
import com.joaopedroduarte.gerenciador.dto.usuario.UsuarioResponseDTO;
import com.joaopedroduarte.gerenciador.entity.Usuario;
import com.joaopedroduarte.gerenciador.repository.UsuarioRepostory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepostory repostory;
    @Autowired
    private AutenticacaoService autenticacaoService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsuarioMapper usuarioMapper;

    public UsuarioResponseDTO cadastrarUsuario(UsuarioCreateDTO dto){
        if(repostory.existsByEmail(dto.getEmail())){
            throw new ResponseStatusException(HttpStatusCode.valueOf(409), "O e-mail informado já está cadastrado");
        }
        if(repostory.existsByCpf(dto.getCpf())){
            throw new ResponseStatusException(HttpStatusCode.valueOf(409), "O cpf informado já está cadastrado");
        }

        // Realizar decodificação da senha para inserção no banco
        String senhaHashed = passwordEncoder.encode(dto.getSenha());
        dto.setSenha(senhaHashed);

        Usuario usuario = usuarioMapper.toEntity(dto);

        repostory.save(usuario);

        return usuarioMapper.toResponseDTO(usuario);
    }

    public Usuario buscarUsuarioPorToken(String token){
        Long idUsuario = autenticacaoService.buscarIdUsuarioPorToken(token);
        Optional<Usuario> usuario = repostory.findById(idUsuario);

        if(usuario.isEmpty()){
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "O usuário informado não existe");
        }

        return usuario.get();
    }

    public Usuario buscarUsuarioPorId(Long idUsuario){
        Optional<Usuario> usuario = repostory.findById(idUsuario);
        if(usuario.isEmpty()){
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Usuário não encontrado");
        }

        return usuario.get();
    }
}
