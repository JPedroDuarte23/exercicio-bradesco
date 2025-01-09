package com.joaopedroduarte.gerenciador.controller;

import com.joaopedroduarte.gerenciador.dto.transacao.TransacaoCreateDTO;
import com.joaopedroduarte.gerenciador.entity.Transacao;
import com.joaopedroduarte.gerenciador.service.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.ResponseEntity.status;

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
        return status(200).body(service.getTransacoesPorUsuario(token));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transacao> retornaUmaTransacao(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token
    ) {
        return status(200).body(service.getTransacao(id, token));
    }

    @PostMapping
    public ResponseEntity<Transacao> registrarTransacao(
            @RequestBody @Valid TransacaoCreateDTO transacaoCreateDTO,
            @RequestHeader("Authorization") String token
    ) {
        return status(202).body(service.postTransacao(transacaoCreateDTO, token));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transacao> alterarTransacao(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token
    ) {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> excluirTransacao(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token
    ) {
        return null;
    }
}
