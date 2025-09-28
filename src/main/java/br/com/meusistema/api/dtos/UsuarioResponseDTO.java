package br.com.meusistema.api.dtos;

import br.com.meusistema.api.enums.Roles;

public record UsuarioResponseDTO(
        String username,
        String password,
        Roles role
) {
}
