package br.com.meusistema.api.dtos;

public record ClienteResponseDTO(
        Long id,
        String nome,
        String email,
        String cpf,
        EnderecoDTO endereco
) {
}
