package com.joaopedroduarte.gerenciador.mapper;

import com.joaopedroduarte.gerenciador.dto.usuario.UsuarioCreateDTO;
import com.joaopedroduarte.gerenciador.dto.usuario.UsuarioResponseDTO;
import com.joaopedroduarte.gerenciador.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioCreateDTO dto){
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setCpf(dto.getCpf());
        usuario.setSenha(dto.getSenha());
        return usuario;
    }

    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        UsuarioResponseDTO responseDTO = new UsuarioResponseDTO();
        responseDTO.setNome(usuario.getNome());
        responseDTO.setEmail(usuario.getEmail());
        responseDTO.setCpf(usuario.getCpf());
        return responseDTO;
    }
}
