package com.joaopedroduarte.gerenciador.service;

import com.joaopedroduarte.gerenciador.dto.usuario.UsuarioCreateDTO;
import com.joaopedroduarte.gerenciador.dto.usuario.UsuarioResponseDTO;
import com.joaopedroduarte.gerenciador.entity.Usuario;
import com.joaopedroduarte.gerenciador.mapper.UsuarioMapper;
import com.joaopedroduarte.gerenciador.repository.UsuarioRepostory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepostory repostory;

    @Mock
    private AutenticacaoService autenticacaoService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioService usuarioService;


    @Test
    @DisplayName("Deve retornar 409, caso e-mail já cadastrado")
    void testCadastrarUsuario_EmailJaCadastrado() {
        UsuarioCreateDTO dto = new UsuarioCreateDTO("Teste", "test@test.com", "12345678901", "12345678");
        when(repostory.existsByEmail(dto.getEmail())).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            usuarioService.cadastrarUsuario(dto);
        });
        assertEquals(409, exception.getStatusCode().value());
        assertEquals("O e-mail informado já está cadastrado", exception.getReason());
    }

    @Test
    @DisplayName("Deve retornar 409, caso cpf já cadastrado")
    void testCadastrarUsuario_CpfJaCadastrado() {
        UsuarioCreateDTO dto = new UsuarioCreateDTO("Teste", "test@test.com", "12345678901", "12345678");
        when(repostory.existsByEmail(dto.getEmail())).thenReturn(false);
        when(repostory.existsByCpf(dto.getCpf())).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            usuarioService.cadastrarUsuario(dto);
        });
        assertEquals(409, exception.getStatusCode().value());
        assertEquals("O e-mail informado já está cadastrado", exception.getReason());
    }

    @Test
    @DisplayName("Deve retornar dto de resposta de cadastro, caso dados válidos")
    void testCadastrarUsuario_Success() {
        UsuarioCreateDTO dto = new UsuarioCreateDTO("Teste", "test@test.com", "12345678901", "12345678");
        Usuario usuario = new Usuario();
        UsuarioResponseDTO responseDTO = new UsuarioResponseDTO();
        responseDTO.setNome(dto.getNome());
        responseDTO.setCpf(dto.getCpf());
        responseDTO.setEmail(dto.getEmail());

        when(repostory.existsByEmail(dto.getEmail())).thenReturn(false);
        when(repostory.existsByCpf(dto.getCpf())).thenReturn(false);
        when(passwordEncoder.encode(dto.getSenha())).thenReturn("senhaCriptografada");
        when(usuarioMapper.toEntity(dto)).thenReturn(usuario);
        when(repostory.save(usuario)).thenReturn(usuario);
        when(usuarioMapper.toResponseDTO(usuario)).thenReturn(responseDTO);

        UsuarioResponseDTO result = usuarioService.cadastrarUsuario(dto);

        assertEquals(dto.getNome(), result.getNome());
        assertEquals(dto.getCpf(), result.getCpf());
        assertEquals(dto.getEmail(), result.getEmail());
        verify(repostory, times(1)).save(usuario);

    }

    @Test
    @DisplayName("Deve retornar 404, caso id no token inválido")
    void testBuscarUsuarioPorToken_UsuarioNaoExistente() {
        String token = "tokenInvalido";
        when(autenticacaoService.buscarIdUsuarioPorToken(token)).thenReturn(1L);
        when(repostory.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            usuarioService.buscarUsuarioPorToken(token);
        });

        assertEquals(404, exception.getStatusCode().value());
        assertEquals("O usuário informado não existe", exception.getReason());
    }

    @Test
    @DisplayName("Deve retornar o usuario, caso id no token válido")
    void testBuscarUsuarioPorToken_Success() {
        String token = "tokenValido";
        Usuario usuario = new Usuario();
        usuario.setEmail("Teste@teste.com");
        usuario.setSenha("123456789");
        usuario.setId(1L);

        when(autenticacaoService.buscarIdUsuarioPorToken(token)).thenReturn(1L);
        when(repostory.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario result = usuarioService.buscarUsuarioPorToken(token);

        assertEquals(usuario.getId(), result.getId());
        assertEquals(usuario.getNome(), result.getNome());
        assertEquals(usuario.getEmail(), result.getEmail());
        assertEquals(usuario.getSenha(), result.getSenha());
    }

    @Test
    @DisplayName("Deve retornar 404, caso id não seja encontrado no banco")
    void testBuscarUsuarioPorId_UsuarioNaoExistente() {
        Long usuarioId = 1L;
        when(repostory.findById(usuarioId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            usuarioService.buscarUsuarioPorId(usuarioId);
        });
        assertEquals(404, exception.getStatusCode().value());
        assertEquals("Usuário não encontrado", exception.getReason());
    }

    @Test
    @DisplayName("Deve retornar o usuario, caso id válido")
    void testBuscarUsuarioPorId_Success() {
        Long usuarioId = 1L;
        Usuario usuario = new Usuario();
        usuario.setEmail("Teste@teste.com");
        usuario.setSenha("123456789");
        usuario.setId(1L);
        when(repostory.findById(usuarioId)).thenReturn(Optional.of(usuario));

        Usuario result = usuarioService.buscarUsuarioPorId(usuarioId);

        assertEquals(usuario.getId(), result.getId());
        assertEquals(usuario.getNome(), result.getNome());
        assertEquals(usuario.getEmail(), result.getEmail());
        assertEquals(usuario.getSenha(), result.getSenha());
    }
}
