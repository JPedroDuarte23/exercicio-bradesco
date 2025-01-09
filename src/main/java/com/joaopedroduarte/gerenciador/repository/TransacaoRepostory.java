package com.joaopedroduarte.gerenciador.repository;

import com.joaopedroduarte.gerenciador.entity.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransacaoRepostory extends JpaRepository<Transacao, Long> {
    List<Transacao> findByUsuarioId(Long usuarioId);

}
