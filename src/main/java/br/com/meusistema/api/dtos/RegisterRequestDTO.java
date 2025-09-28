package br.com.meusistema.api.dtos;

import br.com.meusistema.api.enums.Roles;

public record RegisterRequestDTO(
        String username,
        String password,
        String email,
        Roles role
)

{
}
