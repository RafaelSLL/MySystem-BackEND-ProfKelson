package br.com.meusistema.api.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record ClienteRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100)
        String nome,

        @Email
        String email,

        @CPF
        String cpf,

        @Valid
        EnderecoDTO endereco
) {
}
