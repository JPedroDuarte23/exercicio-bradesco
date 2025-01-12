package com.joaopedroduarte.gerenciador.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.joaopedroduarte.gerenciador.enums.TipoTransacao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoTransacao tipo;

    @Column(nullable = false)
    private Double valor;

    @Column(nullable = true)
    private String descricao;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private Usuario usuario;

}
