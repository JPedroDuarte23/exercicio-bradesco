package com.joaopedroduarte.gerenciador.service;

import com.joaopedroduarte.gerenciador.enums.TipoTransacao;
import com.joaopedroduarte.gerenciador.dto.transacao.Relatorio;
import com.joaopedroduarte.gerenciador.dto.transacao.TransacaoCreateDTO;
import com.joaopedroduarte.gerenciador.dto.transacao.TransacaoPatchDTO;
import com.joaopedroduarte.gerenciador.dto.transacao.TransacaoPutDTO;
import com.joaopedroduarte.gerenciador.entity.Transacao;
import com.joaopedroduarte.gerenciador.entity.Usuario;
import com.joaopedroduarte.gerenciador.repository.TransacaoRepostory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepostory repostory;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AutenticacaoService autenticacaoService;

    public List<Transacao> buscarTransacoesPorUsuario(String token){
        Long idUsuario = autenticacaoService.buscarIdUsuarioPorToken(token);

        return repostory.findByUsuarioId(idUsuario);
    }

    public Transacao buscarTransacao(Long idTransacao, String token){
        Long idUsuario = autenticacaoService.buscarIdUsuarioPorToken(token);
        Optional<Transacao> transacao = repostory.findByUsuarioIdAndId(idUsuario, idTransacao);

        if(transacao.isEmpty()){
            throw new ResponseStatusException(HttpStatusCode.valueOf(404));
        }

        return transacao.get();
    }

    public Transacao registrarTransacao(TransacaoCreateDTO dto, String token){
        Usuario usuario = usuarioService.buscarUsuarioPorToken(token);

        if(dto.getTipo().equals(TipoTransacao.DESPESA)){
            dto.setValor(dto.getValor() * -1);
        }

        Transacao transacao = new Transacao();
        transacao.setUsuario(usuario);
        transacao.setValor(dto.getValor());
        transacao.setTipo(dto.getTipo());
        transacao.setDescricao(dto.getDescricao());
        transacao.setData(dto.getData());

        return repostory.save(transacao);
    }

    public Transacao alterarTransacaoTotal(Long id, TransacaoPutDTO dto, String token){
        Long idUsuario = autenticacaoService.buscarIdUsuarioPorToken(token);
        Optional<Transacao> transacao = repostory.findByUsuarioIdAndId(idUsuario, id);

        if(transacao.isEmpty()){
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Transação não encontrada");
        }
        if(dto.getTipo().equals(TipoTransacao.DESPESA)){
            dto.setValor(dto.getValor() * -1);
        }

        transacao.get().setValor(dto.getValor());
        transacao.get().setTipo(dto.getTipo());
        transacao.get().setDescricao(dto.getDescricao());
        transacao.get().setData(dto.getData());

        return repostory.save(transacao.get());
    }

    public Transacao alterarTransacaoParcial(Long id, TransacaoPatchDTO dto, String token){
        Long idUsuario = autenticacaoService.buscarIdUsuarioPorToken(token);
        Optional<Transacao> transacao = repostory.findByUsuarioIdAndId(idUsuario, id);

        if(transacao.isEmpty()){
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Transação não encontrada");
        }

        // Verificando se os atributos possuem ou não alguma alteração

        if (dto.getTipo() != null) {
            if (dto.getTipo().equals(TipoTransacao.DESPESA) &&
                    (dto.getValor() != null || !dto.getTipo().equals(transacao.get().getTipo()))) {
                dto.setValor(dto.getValor() != null ? dto.getValor() * -1 : transacao.get().getValor() * -1);
            }
            transacao.get().setTipo(dto.getTipo());
        }

        if (dto.getValor() != null) {
            transacao.get().setValor(dto.getValor());
        }

        if(dto.getDescricao() != null){
            transacao.get().setDescricao(dto.getDescricao());
        }

        if(dto.getData() != null){
            transacao.get().setData(dto.getData());
        }

        return repostory.save(transacao.get());
    }

    public void deletarTransacao(String token, Long id){
        Long idUsuario = autenticacaoService.buscarIdUsuarioPorToken(token);
        Optional<Transacao> transacao = repostory.findByUsuarioIdAndId(idUsuario, id);

        if(transacao.isEmpty()){
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Transação não encontrada");
        }

        repostory.deleteById(id);
    }

    public Relatorio gerarRelatorio(LocalDate dataInicio, LocalDate dataFim, String token){
        Long usuarioId = autenticacaoService.buscarIdUsuarioPorToken(token);

        if(!asDatasSaoValidas(dataInicio, dataFim)){
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "A data de inicio não deve ser maior que a data de fim do período informado");
        }

        // Convertendo o LocalDate para LocalDateTime, formato aceito pelo banco
        LocalDateTime dataInicioFormatada = dataInicio.atStartOfDay();
        LocalDateTime dataFimFormatada = dataFim.atTime(LocalTime.MAX);

        List<Transacao> lista = repostory.findByUsuarioIdAndDataBetween(usuarioId, dataInicioFormatada, dataFimFormatada);

        Double saldo = 0.0;
        Double despesas = 0.0;
        Double receitas = 0.0;

        for (int i = 0; i < lista.size(); i++) {
            saldo += lista.get(i).getValor();
            if(lista.get(i).getTipo().equals(TipoTransacao.DESPESA)){
                despesas += lista.get(i).getValor();
            } else {
                receitas += lista.get(i).getValor();
            }
        }

        // Formatação para duas casas decimais para evitar resultados como: 3138.79999999997
        Relatorio relatorio =
                new Relatorio(
                        String.format("%.2f", saldo),
                        String.format("%.2f", despesas),
                        String.format("%.2f", receitas)
                        );

        return relatorio;
    }

    public boolean asDatasSaoValidas(LocalDate dataInicio, LocalDate dataFim){
        if(dataInicio == null || dataFim == null){
            return false;
        }
        return dataFim.isAfter(dataInicio) || dataFim.isEqual(dataInicio);
    }
}
