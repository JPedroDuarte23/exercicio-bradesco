package com.joaopedroduarte.gerenciador.dto.transacao;

import com.joaopedroduarte.gerenciador.enums.TipoTransacao;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransacaoPutDTO {

    @NotNull
    private TipoTransacao tipo;
    @DecimalMin(value = "0.01")
    private Double valor;
    private String descricao;
    @NotNull
    private LocalDateTime data;
}
