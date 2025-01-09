package com.joaopedroduarte.gerenciador.dto.transacao;

import com.joaopedroduarte.gerenciador.TipoTransacao;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransacaoCreateDTO {

    @NotNull
    private TipoTransacao tipo;
    @NotNull
    private Double valor;

    private String descricao;

    @NotNull
    private LocalDateTime data;
}
