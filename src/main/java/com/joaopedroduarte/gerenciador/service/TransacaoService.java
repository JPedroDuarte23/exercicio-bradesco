package com.joaopedroduarte.gerenciador.service;

import com.joaopedroduarte.gerenciador.dto.transacao.TransacaoCreateDTO;
import com.joaopedroduarte.gerenciador.entity.Transacao;
import com.joaopedroduarte.gerenciador.entity.Usuario;
import com.joaopedroduarte.gerenciador.repository.TransacaoRepostory;
import com.joaopedroduarte.gerenciador.repository.UsuarioRepostory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public List<Transacao> getTransacoesPorUsuario(String token){
        Long idUsuario = autenticacaoService.getIdByToken(token);

        return repostory.findByUsuarioId(idUsuario);
    }

    public Transacao getTransacao(Long idTransacao, String token){
        autenticacaoService.validarToken(token);
        Optional<Transacao> transacao = repostory.findById(idTransacao);

        if(transacao.isEmpty()){
            throw new ResponseStatusException(HttpStatusCode.valueOf(404));
        }

        return transacao.get();
    }

    public Transacao postTransacao(TransacaoCreateDTO dto, String token){
        Usuario usuario = usuarioService.getUsuarioByToken(token);

        Transacao transacao = new Transacao();
        transacao.setUsuario(usuario);
        transacao.setValor(dto.getValor());
        transacao.setTipo(dto.getTipo());
        transacao.setDescricao(dto.getDescricao());
        transacao.setData(dto.getData());

        repostory.save(transacao);
        return repostory.save(transacao);
    }

}
