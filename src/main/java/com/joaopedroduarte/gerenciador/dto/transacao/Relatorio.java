package com.joaopedroduarte.gerenciador.dto.transacao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Relatorio {

    private String saldoTotal;
    private String despesaTotal;
    private String receitaTotal;

}
