package com.joaopedroduarte.gerenciador.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        // Limpa o contexto de segurança antes de cada teste
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Deve permitir a autenticação com token válido no filtro")
    void testDoFilterInternal_TokenValido() throws Exception {
        String token = "tokenValido";
        Long userId = 1L;
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtTokenProvider.isTokenValid(token)).thenReturn(true);
        when(jwtTokenProvider.getIdByToken(token)).thenReturn(userId);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        ArgumentCaptor<UsernamePasswordAuthenticationToken> authenticationCaptor = ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);

        // Verifica criação do filtro e se o ID do usuário fi passado
        verify(filterChain).doFilter(request, response);
        verify(jwtTokenProvider).getIdByToken(token);

        SecurityContext context = SecurityContextHolder.getContext();

        // Verifica se a autenticação foi criada e se o ID do usuário está correto
        assertNotNull(context.getAuthentication());
        assertEquals(userId, context.getAuthentication().getPrincipal());
    }

    @Test
    @DisplayName("Não deve permitir autenticação com token inválido no filtro")
    void testDoFilterInternal_TokenInvalido() throws Exception {
        String token = "tokenInvalido";
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtTokenProvider.isTokenValid(token)).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Não deve permitir autenticação sem token")
    void testDoFilterInternal_SemToken() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
