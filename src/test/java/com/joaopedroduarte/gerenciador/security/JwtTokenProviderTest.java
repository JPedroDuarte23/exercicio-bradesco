package com.joaopedroduarte.gerenciador.security;

import com.joaopedroduarte.gerenciador.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private Usuario usuarioMock;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "palavraPasseToken", "chave-secreta");
        ReflectionTestUtils.setField(jwtTokenProvider, "tempoDeExpiracao", 3600000);
    }

    @Test
    @DisplayName("Deve retornar o token")
    void testGenerarTokenForUsuario() {
        when(usuarioMock.getId()).thenReturn(1L);

        String token = jwtTokenProvider.generarTokenForUsuario(usuarioMock);

        assertNotNull(token);
        assertTrue(token.startsWith("eyJ"));
    }

    @Test
    @DisplayName("Deve retornar o id por trás do token")
    void testGetIdByToken() {
        when(usuarioMock.getId()).thenReturn(1L);
        String token = jwtTokenProvider.generarTokenForUsuario(usuarioMock);

        Long id = jwtTokenProvider.getIdByToken(token);

        assertNotNull(id);
        assertEquals(1L, id);
    }

    @Test
    @DisplayName("Deve retornar verdadeiro, caso token válido")
    void testIsTokenValid_ValidToken() {
        String token = jwtTokenProvider.generarTokenForUsuario(usuarioMock);

        boolean isValid = jwtTokenProvider.isTokenValid(token);

        assertTrue(isValid);
    }

    @Test
    @DisplayName("Deve retornar verdadeiro, caso token inválido")
    void testIsTokenValid_InvalidToken() {
        String invalidToken = "tokenInvído";

        boolean isValid = jwtTokenProvider.isTokenValid(invalidToken);

        assertFalse(isValid);
    }
}
