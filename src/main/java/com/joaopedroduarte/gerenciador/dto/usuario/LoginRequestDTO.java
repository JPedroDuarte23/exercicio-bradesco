package com.joaopedroduarte.gerenciador.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequestDTO {

    @Email(message = "O e-mail precisa estar em um formato válido")
    private String email;

    @NotBlank(message = "Senha não pode estar vazia")
    private String senha;
}
