package com.joaopedroduarte.gerenciador.controller;

import com.joaopedroduarte.gerenciador.dto.transacao.Relatorio;
import com.joaopedroduarte.gerenciador.dto.transacao.TransacaoCreateDTO;
import com.joaopedroduarte.gerenciador.dto.transacao.TransacaoPatchDTO;
import com.joaopedroduarte.gerenciador.dto.transacao.TransacaoPutDTO;
import com.joaopedroduarte.gerenciador.entity.Transacao;
import com.joaopedroduarte.gerenciador.enums.TipoTransacao;
import com.joaopedroduarte.gerenciador.service.TransacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TransacaoControllerTest {

    @Mock
    private TransacaoService service;

    @InjectMocks
    private TransacaoController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @DisplayName("Deve retornar todas as transações com sucesso")
    @Test
    void testRetornarTodasAsTransacoes() throws Exception {
        when(service.buscarTransacoesPorUsuario(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/transacoes")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(service, times(1)).buscarTransacoesPorUsuario(anyString());
    }

    @DisplayName("Deve retornar uma transação específica com sucesso")
    @Test
    void testRetornaUmaTransacao() throws Exception {
        Transacao transacao = new Transacao();
        transacao.setId(1L);

        when(service.buscarTransacao(anyLong(), anyString())).thenReturn(transacao);

        mockMvc.perform(get("/api/transacoes/{id}", 1L)
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(service, times(1)).buscarTransacao(anyLong(), anyString());
    }

    @DisplayName("Deve registrar uma transação com sucesso")
    @Test
    void testRegistrarTransacao() throws Exception {
        Transacao transacao = new Transacao();
        transacao.setId(1L);
        transacao.setTipo(TipoTransacao.RECEITA);
        transacao.setValor(250.00);
        transacao.setDescricao("Pagamento de serviços");
        transacao.setData(LocalDateTime.now());

        when(service.registrarTransacao(any(), anyString())).thenReturn(transacao);

        mockMvc.perform(post("/api/transacoes")
                        .contentType("application/json")
                        .header("Authorization", "token")
                        .content("{\n" +
                                "  \"tipo\": \"RECEITA\",\n" +
                                "  \"valor\": 250.00,\n" +
                                "  \"descricao\": \"Pagamento de serviços\",\n" +
                                "  \"data\": \"2025-01-12T10:30:00\"\n" +
                                "}\n"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.valor").value(250.0))
                .andExpect(jsonPath("$.tipo").value("RECEITA"))
                .andExpect(jsonPath("$.descricao").value("Pagamento de serviços"));

        verify(service, times(1)).registrarTransacao(any(), anyString());
    }

    @DisplayName("Deve alterar uma transação com sucesso")
    @Test
    void testAlterarTransacao() throws Exception {
        Transacao transacao = new Transacao();
        transacao.setId(1L);
        transacao.setTipo(TipoTransacao.DESPESA);
        transacao.setValor(-250.00);
        transacao.setDescricao("UBER");
        transacao.setData(LocalDateTime.now());

        when(service.alterarTransacaoTotal(anyLong(), any(), anyString())).thenReturn(transacao);

        mockMvc.perform(put("/api/transacoes/{id}", 1)
                        .contentType("application/json")
                        .header("Authorization", "token")
                        .content("{\n" +
                                "  \"tipo\": \"DESPESA\",\n" +
                                "  \"valor\": 250.00,\n" +
                                "  \"descricao\": \"UBER\",\n" +
                                "  \"data\": \"2025-01-12T10:30:00\"\n" +
                                "}\n"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.valor").value(-250.0))
                .andExpect(jsonPath("$.tipo").value("DESPESA"))
                .andExpect(jsonPath("$.descricao").value("UBER"));

        verify(service, times(1)).alterarTransacaoTotal(anyLong(), any(), anyString());
    }

    @DisplayName("Deve alterar parcialmente uma transação com sucesso")
    @Test
    void testAlterarTransacaoParcial() throws Exception {
        Transacao transacao = new Transacao();
        transacao.setId(1L);
        transacao.setTipo(TipoTransacao.RECEITA);
        transacao.setValor(2043.00);
        transacao.setDescricao("Salario Bradesco Dezembro/2024");
        transacao.setData(LocalDateTime.now());

        when(service.alterarTransacaoParcial(anyLong(), any(), anyString())).thenReturn(transacao);

        mockMvc.perform(patch("/api/transacoes/{id}", 1)
                        .contentType("application/json")
                        .header("Authorization", "token")
                        .content("{\n" +
                                "  \"descricao\": \"Salario Bradesco Dezembro/2024\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.valor").value(2043.00))
                .andExpect(jsonPath("$.tipo").value("RECEITA"))
                .andExpect(jsonPath("$.descricao").value("Salario Bradesco Dezembro/2024"));

        verify(service, times(1)).alterarTransacaoParcial(anyLong(), any(), anyString());
    }

    @DisplayName("Deve excluir uma transação com sucesso")
    @Test
    void testExcluirTransacao() throws Exception {
        doNothing().when(service).deletarTransacao(anyString(), anyLong());

        mockMvc.perform(delete("/api/transacoes/{id}", 1)
                        .header("Authorization", "token"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deletarTransacao(anyString(), anyLong());
    }

    @DisplayName("Deve gerar um relatório com sucesso")
    @Test
    void testGerarRelatorio() throws Exception {
        Relatorio relatorio = new Relatorio();
        relatorio.setSaldoTotal("200,00");
        relatorio.setReceitaTotal("300,00");
        relatorio.setDespesaTotal("-100,00");

        when(service.gerarRelatorio(any(), any(), anyString())).thenReturn(relatorio);

        mockMvc.perform(get("/api/transacoes/relatorio")
                        .param("dataInicio", "2025-01-01")
                        .param("dataFim", "2025-01-31")
                        .header("Authorization", "token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldoTotal").value("200,00"))
                .andExpect(jsonPath("$.receitaTotal").value("300,00"))
                .andExpect(jsonPath("$.despesaTotal").value("-100,00"));

        verify(service, times(1)).gerarRelatorio(any(), any(), anyString());
    }
}
