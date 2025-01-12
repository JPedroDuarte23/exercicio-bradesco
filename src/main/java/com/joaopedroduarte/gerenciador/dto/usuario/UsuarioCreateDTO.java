package com.joaopedroduarte.gerenciador.dto.usuario;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioCreateDTO {

    @NotBlank(message = "O nome não pode estar vazio")
    private String nome;
    @Email(message = "O e-mail precisa estar em um formato válido")
    private String email;
    @CPF(message = "O CPF precisa ser válido")
    private String cpf;
    @Size(min = 8, max = 20, message = "A senha deve ter entre 8 e 20 caracteres.")
    private String senha;
}
