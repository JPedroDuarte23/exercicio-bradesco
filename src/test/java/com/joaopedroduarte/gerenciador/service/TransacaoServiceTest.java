package com.joaopedroduarte.gerenciador.service;

import com.joaopedroduarte.gerenciador.dto.transacao.Relatorio;
import com.joaopedroduarte.gerenciador.dto.transacao.TransacaoCreateDTO;
import com.joaopedroduarte.gerenciador.dto.transacao.TransacaoPatchDTO;
import com.joaopedroduarte.gerenciador.dto.transacao.TransacaoPutDTO;
import com.joaopedroduarte.gerenciador.entity.Transacao;
import com.joaopedroduarte.gerenciador.entity.Usuario;
import com.joaopedroduarte.gerenciador.enums.TipoTransacao;
import com.joaopedroduarte.gerenciador.repository.TransacaoRepostory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransacaoServiceTest {

    @InjectMocks
    private TransacaoService transacaoService;

    @Mock
    private TransacaoRepostory repostory;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private AutenticacaoService autenticacaoService;

    private static final String TOKEN = "token-teste";
    private static final Long USER_ID = 1L;
    private static final Long TRANSACAO_ID = 1L;


    @Test
    @DisplayName("Deve retornar uma lista de Transacoes, caso token válido")
    void testBuscarTransacoesPorUsuario() {
        List<Transacao> listaMock = new ArrayList<>();
        Transacao transacao1 = new Transacao();
        transacao1.setTipo(TipoTransacao.RECEITA);
        transacao1.setValor(250.00);
        Transacao transacao2 = new Transacao();
        transacao2.setTipo(TipoTransacao.DESPESA);
        transacao2.setValor(-150.00);
        listaMock.add(transacao1);
        listaMock.add(transacao2);

        when(autenticacaoService.buscarIdUsuarioPorToken(TOKEN)).thenReturn(USER_ID);
        when(repostory.findByUsuarioId(USER_ID)).thenReturn(listaMock);

        List<Transacao> result = transacaoService.buscarTransacoesPorUsuario(TOKEN);

        assertEquals(listaMock, result);
    }

    @Test
    @DisplayName("Deve retornar uma lista de Transacoes, caso token válido e id da transacao valido")
    void testBuscarTransacao() {
        Transacao transacaoMock = new Transacao();
        when(autenticacaoService.buscarIdUsuarioPorToken(TOKEN)).thenReturn(USER_ID);
        when(repostory.findByUsuarioIdAndId(USER_ID, TRANSACAO_ID)).thenReturn(Optional.of(transacaoMock));

        var result = transacaoService.buscarTransacao(TRANSACAO_ID, TOKEN);

        assertEquals(transacaoMock, result);
    }

    @Test
    @DisplayName("Deve retornar 404, caso id da transacao inválido")
    void testBuscarTransacaoNaoEncontrada() {
        when(autenticacaoService.buscarIdUsuarioPorToken(TOKEN)).thenReturn(USER_ID);
        when(repostory.findByUsuarioIdAndId(USER_ID, TRANSACAO_ID)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> transacaoService.buscarTransacao(TRANSACAO_ID, TOKEN));
    }

    @Test
    @DisplayName("Deve retornar a transacao(receita) cadastrada")
    void testRegistrarTransacao_Receita() {
        TransacaoCreateDTO dto = new TransacaoCreateDTO(TipoTransacao.RECEITA, 100.0, "Venda", LocalDateTime.now());
        Transacao transacao1 = new Transacao();
        transacao1.setId(TRANSACAO_ID);
        transacao1.setTipo(dto.getTipo());
        transacao1.setData(dto.getData());
        transacao1.setDescricao(dto.getDescricao());
        transacao1.setValor(dto.getValor());

        Usuario usuarioMock = new Usuario();
        when(usuarioService.buscarUsuarioPorToken(TOKEN)).thenReturn(usuarioMock);
        when(repostory.save(any(Transacao.class))).thenReturn(transacao1);

        Transacao result = transacaoService.registrarTransacao(dto, TOKEN);

        assertEquals(TRANSACAO_ID, result.getId());
        assertEquals(dto.getValor(), result.getValor());
        assertEquals(dto.getData(), result.getData());
        assertEquals(dto.getDescricao(), result.getDescricao());
        assertEquals(dto.getTipo(), result.getTipo());
    }

    @Test
    @DisplayName("Deve retornar a transacao(despesa) cadastrada")
    void testRegistrarTransacao_Despesa() {
        TransacaoCreateDTO dto = new TransacaoCreateDTO(TipoTransacao.DESPESA, 100.0, "Compra", LocalDateTime.now());
        Transacao transacao1 = new Transacao();
        transacao1.setId(TRANSACAO_ID);
        transacao1.setTipo(dto.getTipo());
        transacao1.setData(dto.getData());
        transacao1.setDescricao(dto.getDescricao());
        transacao1.setValor(dto.getValor() * -1);

        Usuario usuarioMock = new Usuario();
        when(usuarioService.buscarUsuarioPorToken(TOKEN)).thenReturn(usuarioMock);
        when(repostory.save(any(Transacao.class))).thenReturn(transacao1);

        var result = transacaoService.registrarTransacao(dto, TOKEN);

        assertEquals(TRANSACAO_ID, result.getId());
        assertEquals(dto.getValor(), result.getValor());
        assertEquals(dto.getData(), result.getData());
        assertEquals(dto.getDescricao(), result.getDescricao());
        assertEquals(dto.getTipo(), result.getTipo());
    }

    @Test
    @DisplayName("Deve retornar a transacao(receita) alterada")
    void testAlterarTransacaoTotal_Receita() {
        TransacaoPutDTO dto = new TransacaoPutDTO(TipoTransacao.RECEITA, 200.0, "Novo pagamento", LocalDateTime.now());
        Transacao transacao1 = new Transacao();
        transacao1.setId(TRANSACAO_ID);
        transacao1.setTipo(dto.getTipo());
        transacao1.setData(dto.getData());
        transacao1.setDescricao(dto.getDescricao());
        transacao1.setValor(dto.getValor());

        when(autenticacaoService.buscarIdUsuarioPorToken(TOKEN)).thenReturn(USER_ID);
        when(repostory.findByUsuarioIdAndId(USER_ID, TRANSACAO_ID)).thenReturn(Optional.of(transacao1));
        when(repostory.save(any(Transacao.class))).thenReturn(transacao1);

        Transacao result = transacaoService.alterarTransacaoTotal(TRANSACAO_ID, dto, TOKEN);

        assertEquals(dto.getValor(), result.getValor());
        assertEquals(dto.getData(), result.getData());
        assertEquals(dto.getDescricao(), result.getDescricao());
        assertEquals(dto.getTipo(), result.getTipo());
    }

    @Test
    @DisplayName("Deve retornar a transacao(despesa) alterada")
    void testAlterarTransacaoTotal_Despesa() {
        TransacaoPutDTO dto = new TransacaoPutDTO(TipoTransacao.DESPESA, 200.0, "Nova compra", LocalDateTime.now());
        Transacao transacao1 = new Transacao();
        transacao1.setId(TRANSACAO_ID);
        transacao1.setTipo(dto.getTipo());
        transacao1.setData(dto.getData());
        transacao1.setDescricao(dto.getDescricao());
        transacao1.setValor(dto.getValor());

        when(autenticacaoService.buscarIdUsuarioPorToken(TOKEN)).thenReturn(USER_ID);
        when(repostory.findByUsuarioIdAndId(USER_ID, TRANSACAO_ID)).thenReturn(Optional.of(transacao1));
        when(repostory.save(any(Transacao.class))).thenReturn(transacao1);

        Transacao result = transacaoService.alterarTransacaoTotal(TRANSACAO_ID, dto, TOKEN);

        assertEquals(dto.getValor(), result.getValor());
        assertEquals(dto.getData(), result.getData());
        assertEquals(dto.getDescricao(), result.getDescricao());
        assertEquals(dto.getTipo(), result.getTipo());
    }


    @Test
    @DisplayName("Deve retornar 404, caso o id não exista ou a transacao do id informado não esteja registrada no id do usuário")
    void testAlterarTransacaoTotalNaoEncontrada() {
        TransacaoPutDTO dto = new TransacaoPutDTO(TipoTransacao.RECEITA, 200.0, "Novo pagamento", LocalDateTime.now());
        when(autenticacaoService.buscarIdUsuarioPorToken(TOKEN)).thenReturn(USER_ID);
        when(repostory.findByUsuarioIdAndId(USER_ID, TRANSACAO_ID)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> transacaoService.alterarTransacaoTotal(TRANSACAO_ID, dto, TOKEN));
    }

    @Test
    @DisplayName("Deve retornar a transacao(receita) parcialmente alterada")
    void testAlterarTransacaoParcial_Receita() {
        TransacaoPatchDTO dto = new TransacaoPatchDTO();

        dto.setTipo(TipoTransacao.RECEITA);
        dto.setValor(1500.00);
        dto.setDescricao("Salário Bradesco");
        dto.setData(LocalDateTime.now().minusDays(5));

        Transacao transacaoMock = new Transacao();
        when(autenticacaoService.buscarIdUsuarioPorToken(TOKEN)).thenReturn(USER_ID);
        when(repostory.findByUsuarioIdAndId(USER_ID, TRANSACAO_ID)).thenReturn(Optional.of(transacaoMock));
        when(repostory.save(any(Transacao.class))).thenReturn(transacaoMock);

        var result = transacaoService.alterarTransacaoParcial(TRANSACAO_ID, dto, TOKEN);

        assertNotNull(result);
        assertEquals(1500.00, result.getValor());
            assertEquals(TipoTransacao.RECEITA, result.getTipo());
        verify(repostory, times(1)).save(any(Transacao.class));
    }

    @Test
    @DisplayName("Deve retornar a transacao(despesa) parcialmente alterada")
    void testAlterarTransacaoParcial_Despesa() {
        TransacaoPatchDTO dto = new TransacaoPatchDTO();

        dto.setTipo(TipoTransacao.DESPESA);
        dto.setValor(250.00);
        dto.setDescricao("Roupas Renner");
        dto.setData(LocalDateTime.now().minusDays(5));

        Transacao transacaoMock = new Transacao();
        when(autenticacaoService.buscarIdUsuarioPorToken(TOKEN)).thenReturn(USER_ID);
        when(repostory.findByUsuarioIdAndId(USER_ID, TRANSACAO_ID)).thenReturn(Optional.of(transacaoMock));
        when(repostory.save(any(Transacao.class))).thenReturn(transacaoMock);

        var result = transacaoService.alterarTransacaoParcial(TRANSACAO_ID, dto, TOKEN);

        assertNotNull(result);
        assertEquals(-250.00, result.getValor());
        assertEquals(TipoTransacao.DESPESA, result.getTipo());
        verify(repostory, times(1)).save(any(Transacao.class));
    }

    @Test
    @DisplayName("Deve retornar 404, caso o id não exista ou a transacao do id informado não esteja registrada no id do usuário")
    void testAlterarTransacaoParcialNaoEncontrada() {
        TransacaoPatchDTO dto = new TransacaoPatchDTO();

        dto.setValor(450.00);
        dto.setDescricao("Compras no shopping");

        when(autenticacaoService.buscarIdUsuarioPorToken(TOKEN)).thenReturn(USER_ID);
        when(repostory.findByUsuarioIdAndId(USER_ID, TRANSACAO_ID)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> transacaoService.alterarTransacaoParcial(TRANSACAO_ID, dto, TOKEN));
    }

    @Test
    @DisplayName("Verifica se o método de deleção foi chamado")
    void testDeletarTransacao() {
        Transacao transacaoMock = new Transacao();
        when(autenticacaoService.buscarIdUsuarioPorToken(TOKEN)).thenReturn(USER_ID);
        when(repostory.findByUsuarioIdAndId(USER_ID, TRANSACAO_ID)).thenReturn(Optional.of(transacaoMock));

        transacaoService.deletarTransacao(TOKEN, TRANSACAO_ID);

        verify(repostory, times(1)).deleteById(TRANSACAO_ID);
    }

    @Test
    @DisplayName("Deve retornar 404, caso o id não exista ou a transacao do id informado não esteja registrada no id do usuário")
    void testDeletarTransacaoNaoEncontrada() {
        when(autenticacaoService.buscarIdUsuarioPorToken(TOKEN)).thenReturn(USER_ID);
        when(repostory.findByUsuarioIdAndId(USER_ID, TRANSACAO_ID)).thenReturn(Optional.empty());

        var excecao = assertThrows(ResponseStatusException.class,
                () -> transacaoService.deletarTransacao(TOKEN, TRANSACAO_ID));

        assertEquals(HttpStatusCode.valueOf(404), excecao.getStatusCode());
        assertEquals("Transação não encontrada", excecao.getReason());
    }

    @Test
    @DisplayName("Deve retornar o relatório")
    void testGerarRelatorio() {
        LocalDate dataInicio = LocalDate.now().minusDays(5);
        LocalDate dataFim = LocalDate.now();

        List<Transacao> listaMock = new ArrayList<>();
        Transacao transacao1 = new Transacao();
        transacao1.setTipo(TipoTransacao.RECEITA);
        transacao1.setValor(250.00);
        Transacao transacao2 = new Transacao();
        transacao2.setTipo(TipoTransacao.DESPESA);
        transacao2.setValor(-150.00);
        listaMock.add(transacao1);
        listaMock.add(transacao2);

        when(autenticacaoService.buscarIdUsuarioPorToken(TOKEN)).thenReturn(USER_ID);
        when(repostory.findByUsuarioIdAndDataBetween(USER_ID, dataInicio.atStartOfDay(), dataFim.atTime(LocalTime.MAX))).thenReturn(listaMock);

        Relatorio result = transacaoService.gerarRelatorio(dataInicio, dataFim, TOKEN);

        assertEquals("100,00", result.getSaldoTotal());
        assertEquals("-150,00", result.getDespesaTotal());
        assertEquals("250,00", result.getReceitaTotal());
    }

    @Test
    @DisplayName("Deve retornar 400, caso a data inicio seja maior que a data fim")
    void testGerarRelatorio_ErroDatas() {
        LocalDate dataInicio = LocalDate.now();
        LocalDate dataFim = LocalDate.now().minusDays(5);

        when(autenticacaoService.buscarIdUsuarioPorToken(TOKEN)).thenReturn(USER_ID);

        var excecao = assertThrows(ResponseStatusException.class, () -> {
                    transacaoService.gerarRelatorio(dataInicio, dataFim, TOKEN);
                });

        assertEquals(HttpStatusCode.valueOf(400), excecao.getStatusCode());
        assertEquals("A data de inicio não deve ser maior que a data de fim do período informado",
                excecao.getReason());
    }

    @Test
    @DisplayName("Deve retornar verdadeiro, caso data inicio e data fim válidas")
    void testAsDatasSaoValidas() {
        LocalDate dataInicio = LocalDate.now().minusDays(5);
        LocalDate dataFim = LocalDate.now();

        assertTrue(transacaoService.asDatasSaoValidas(dataInicio, dataFim));
    }

    @Test
    @DisplayName("Deve retornar falso, caso data inicio e data fim inválidas")
    void testAsDatasNaoSaoValidas() {
        LocalDate dataInicio = LocalDate.now();
        LocalDate dataFim = LocalDate.now().minusDays(1);

        assertFalse(transacaoService.asDatasSaoValidas(dataInicio, dataFim));
    }

    @Test
    @DisplayName("Deve retornar falso, caso data inicio ou data fim nulas")
    void testDatasNulas(){
        LocalDate dataInicio = null;
        LocalDate dataFim = LocalDate.now().minusDays(1);
        assertFalse(transacaoService.asDatasSaoValidas(dataInicio, dataFim));
    }
}
