package com.joaopedroduarte.gerenciador.mapper;

import com.joaopedroduarte.gerenciador.dto.usuario.UsuarioCreateDTO;
import com.joaopedroduarte.gerenciador.dto.usuario.UsuarioResponseDTO;
import com.joaopedroduarte.gerenciador.entity.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioMapperTest {

    private final UsuarioMapper usuarioMapper = new UsuarioMapper();

    @Test
    @DisplayName("Deve transformar o dto de criação do usuário na entidade usuário")
    void testToEntity() {
        UsuarioCreateDTO dto = new UsuarioCreateDTO("João", "joao@test.com", "12345678901", "senha123");

        Usuario usuario = usuarioMapper.toEntity(dto);

        assertNotNull(usuario);
        assertEquals(dto.getNome(), usuario.getNome());
        assertEquals(dto.getEmail(), usuario.getEmail());
        assertEquals(dto.getCpf(), usuario.getCpf());
        assertEquals(dto.getSenha(), usuario.getSenha());
    }

    @Test
    @DisplayName("Deve transformar a entidade usuário no dto de resposta de cadastro    ")
    void testToResponseDTO() {
        Usuario usuario = new Usuario();
        usuario.setNome("João");
        usuario.setEmail("joao@test.com");
        usuario.setCpf("12345678901");

        UsuarioResponseDTO responseDTO = usuarioMapper.toResponseDTO(usuario);

        assertNotNull(responseDTO);
        assertEquals(usuario.getNome(), responseDTO.getNome());
        assertEquals(usuario.getEmail(), responseDTO.getEmail());
        assertEquals(usuario.getCpf(), responseDTO.getCpf());
    }
}
