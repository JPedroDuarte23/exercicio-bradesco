package com.joaopedroduarte.gerenciador.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginRequestDTO {

    @Email(message = "O e-mail precisa estar em um formato válido")
    private String email;

    @NotBlank(message = "A senha não pode estar vazia")
    private String senha;
}
