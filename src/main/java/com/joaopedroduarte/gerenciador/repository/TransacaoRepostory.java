package com.joaopedroduarte.gerenciador.repository;

import com.joaopedroduarte.gerenciador.entity.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransacaoRepostory extends JpaRepository<Transacao, Long> {
    List<Transacao> findByUsuarioId(Long usuarioId);

    Optional<Transacao>findByUsuarioIdAndId(Long usuarioId, Long id);

    @Query("SELECT t FROM Transacao t WHERE t.usuario.id = :usuarioId AND t.data BETWEEN :dataInicio AND :dataFim")
    List<Transacao> findByUsuarioIdAndDataBetween(Long usuarioId, LocalDateTime dataInicio, LocalDateTime dataFim);

}
