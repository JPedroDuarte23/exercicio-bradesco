package com.joaopedroduarte.gerenciador.service;

import com.joaopedroduarte.gerenciador.dto.usuario.LoginRequestDTO;
import com.joaopedroduarte.gerenciador.dto.usuario.LoginResponseDTO;
import com.joaopedroduarte.gerenciador.entity.Usuario;
import com.joaopedroduarte.gerenciador.repository.UsuarioRepostory;
import com.joaopedroduarte.gerenciador.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutenticacaoServiceTest {

    @Mock
    private UsuarioRepostory repostory;

    @Mock
    private JwtTokenProvider provider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AutenticacaoService autenticacaoService;

    private Usuario usuario;
    private LoginRequestDTO loginRequestDTO;
    private LoginResponseDTO loginResponseDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("teste@exemplo.com");
        usuario.setSenha("senhaCodificada");

        loginRequestDTO = new LoginRequestDTO("teste@exemplo.com", "senha");
        loginResponseDTO = new LoginResponseDTO();
    }

    @Test
    void validarLogin_Sucesso(){
        when(repostory.findByEmail(loginRequestDTO.getEmail())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(loginRequestDTO.getSenha(), usuario.getSenha())).thenReturn(true);
        when(provider.generarTokenForUsuario(usuario)).thenReturn("token");

        LoginResponseDTO responseDTO = autenticacaoService.validarLogin(loginRequestDTO);

        assertNotNull(responseDTO);
        assertEquals("token", responseDTO.getToken());
    }

    @Test
    @DisplayName("Deve retornar 401, caso não exista o e-mail na base de dados")
    void validarLogin_ErroEmail(){
        when(repostory.findByEmail(loginRequestDTO.getEmail())).thenReturn(Optional.empty());

        var excecao = assertThrows(ResponseStatusException.class,
                () -> autenticacaoService.validarLogin(loginRequestDTO)
        );

        assertEquals("E-mail ou senha inválidos.", excecao.getReason());
        assertEquals(HttpStatusCode.valueOf(401), excecao.getStatusCode());
    }

    @Test
    @DisplayName("Deve retornar 401, caso não exista a senha esteja incorreta")
    void validarLogin_ErroSenha(){
        when(repostory.findByEmail(loginRequestDTO.getEmail())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(loginRequestDTO.getSenha(), usuario.getSenha())).thenReturn(false);

        var excecao = assertThrows(ResponseStatusException.class,
                () -> autenticacaoService.validarLogin(loginRequestDTO)
        );

        assertEquals("E-mail ou senha inválidos.", excecao.getReason());
        assertEquals(HttpStatusCode.valueOf(401), excecao.getStatusCode());
    }

    @Test
    @DisplayName("Não lança exceção, caso o token seja válido")
    void validarToken_Success() {
        when(provider.isTokenValid("tokenValido")).thenReturn(true);
        autenticacaoService.validarToken("tokenValido");

        verify(provider, times(1)).isTokenValid("tokenValido");
    }

    @Test
    @DisplayName("Deve retornar 401, caso o token seja inválido")
    void validarToken_TokenInvalido() {
        when(provider.isTokenValid("tokenInvalido")).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            autenticacaoService.validarToken("tokenInvalido");
        });

        assertEquals(HttpStatusCode.valueOf(401), exception.getStatusCode());
        assertEquals("Token inválido", exception.getReason());
    }

    @Test
    @DisplayName("Deve retornar o id do Usuario, caso o token seja válido")
    void buscarIdUsuarioPorToken_Success() {
        when(provider.isTokenValid("tokenValido")).thenReturn(true);
        when(provider.getIdByToken("tokenValido")).thenReturn(1L);

        Long usuarioId = autenticacaoService.buscarIdUsuarioPorToken("tokenValido");

        assertEquals(1L, usuarioId);
    }

    @Test
    @DisplayName("Deve retornar 401, caso o token seja inválido")
    void buscarIdUsuarioPorToken_TokenInvalido() {
        // Configurar mocks
        when(provider.isTokenValid("tokenInvalido")).thenReturn(false);

        // Chamar o método e verificar exceção
        var exception = assertThrows(ResponseStatusException.class, () -> {
            autenticacaoService.buscarIdUsuarioPorToken("tokenInvalido");
        });

        assertEquals(HttpStatusCode.valueOf(401), exception.getStatusCode());
        assertEquals("Token inválido", exception.getReason());
    }
}