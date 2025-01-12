package com.joaopedroduarte.gerenciador.controller;

import com.joaopedroduarte.gerenciador.dto.usuario.UsuarioCreateDTO;
import com.joaopedroduarte.gerenciador.dto.usuario.UsuarioResponseDTO;
import com.joaopedroduarte.gerenciador.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService service;

    @InjectMocks
    private UsuarioController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void cadastrarUsuario_DeveRetornarUsuarioResponseDTO() throws Exception {
        UsuarioResponseDTO responseDTO = new UsuarioResponseDTO();
        responseDTO.setNome("João");
        responseDTO.setEmail("joao@exemplo.com");
        responseDTO.setCpf("12345678901");

        when(service.cadastrarUsuario(any())).thenReturn(responseDTO);

        mockMvc.perform(post("/api/usuarios")
                        .contentType("application/json")
                        .content("{\n" +
                                "  \"nome\": \"João\",\n" +
                                "  \"email\": \"joao@exemplo.com\",\n" +
                                "  \"cpf\": \"12345678901\",\n" +
                                "  \"senha\": \"senha123\"\n" +
                                "}\n"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("João"))
                .andExpect(jsonPath("$.email").value("joao@exemplo.com"))
                .andExpect(jsonPath("$.cpf").value("12345678901"));
    }
}
