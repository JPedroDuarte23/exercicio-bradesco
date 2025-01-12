package com.joaopedroduarte.gerenciador.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joaopedroduarte.gerenciador.dto.usuario.LoginRequestDTO;
import com.joaopedroduarte.gerenciador.dto.usuario.LoginResponseDTO;
import com.joaopedroduarte.gerenciador.service.AutenticacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AutenticacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AutenticacaoService autenticacaoService;

    @InjectMocks
    private AutenticacaoController autenticacaoController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(autenticacaoController).build();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("Deve autenticar com sucesso quando as credenciais estiverem corretas")
    @Test
    void testLogin_Success() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("user@example.com");
        request.setSenha("senha123");

        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken("tokenValido");

        when(autenticacaoService.validarLogin(any(LoginRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("tokenValido"));

        verify(autenticacaoService, times(1)).validarLogin(any(LoginRequestDTO.class));
    }

    @DisplayName("Deve retornar erro 400 quando as credenciais estiverem incorretas")
    @Test
    void testLogin_Fail() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("user@example.com");
        request.setSenha("senhaErrada");

        when(autenticacaoService.validarLogin(any(LoginRequestDTO.class))).thenThrow(new ResponseStatusException(HttpStatusCode.valueOf(401), "E-mail ou senha inválidos"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        verify(autenticacaoService, times(1)).validarLogin(any(LoginRequestDTO.class));
    }
}