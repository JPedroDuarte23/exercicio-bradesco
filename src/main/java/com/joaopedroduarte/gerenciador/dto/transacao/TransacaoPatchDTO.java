package com.joaopedroduarte.gerenciador.dto.transacao;

import com.joaopedroduarte.gerenciador.enums.TipoTransacao;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TransacaoPatchDTO {

    private TipoTransacao tipo;
    @DecimalMin(value = "0.01")
    private Double valor;

    private String descricao;

    private LocalDateTime data;
}
