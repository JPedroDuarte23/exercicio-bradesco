package com.joaopedroduarte.gerenciador.controller;

import com.joaopedroduarte.gerenciador.dto.transacao.Relatorio;
import com.joaopedroduarte.gerenciador.dto.transacao.TransacaoCreateDTO;
import com.joaopedroduarte.gerenciador.dto.transacao.TransacaoPatchDTO;
import com.joaopedroduarte.gerenciador.dto.transacao.TransacaoPutDTO;
import com.joaopedroduarte.gerenciador.entity.Transacao;
import com.joaopedroduarte.gerenciador.service.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.ResponseEntity.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/transacoes")
public class TransacaoController {

    @Autowired
    private TransacaoService service;

    @GetMapping
    public ResponseEntity<List<Transacao>> retornarTodasAsTransacoes(
            @RequestHeader("Authorization") String token
    ) {
        return status(200).body(service.buscarTransacoesPorUsuario(token));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transacao> retornaUmaTransacao(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token
    ) {
        return status(200).body(service.buscarTransacao(id, token));
    }

    @PostMapping
    public ResponseEntity<Transacao> registrarTransacao(
            @RequestBody @Valid TransacaoCreateDTO transacaoCreateDTO,
            @RequestHeader("Authorization") String token
    ) {
        return status(201   ).body(service.registrarTransacao(transacaoCreateDTO, token));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transacao> alterarTransacao(
            @PathVariable Long id,
            @RequestBody @Valid TransacaoPutDTO updateDTO,
            @RequestHeader("Authorization") String token
    ) {
        return status(200).body(service.alterarTransacaoTotal(id, updateDTO, token));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Transacao> alterarTransacaoParcial(
            @PathVariable Long id,
            @RequestBody @Valid TransacaoPatchDTO updateDTO,
            @RequestHeader("Authorization") String token
    ){
        return status(200).body(service.alterarTransacaoParcial(id, updateDTO, token));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirTransacao(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token
    ) {
        service.deletarTransacao(token, id);
        return status(204).build();
    }

    @GetMapping("/relatorio")
    public ResponseEntity<Relatorio> gerarRelatorio(
            @RequestParam LocalDate dataInicio,
            @RequestParam LocalDate dataFim,
            @RequestHeader("Authorization") String token
            ){
        return status(200).body(service.gerarRelatorio(dataInicio, dataFim, token));
    }
}
