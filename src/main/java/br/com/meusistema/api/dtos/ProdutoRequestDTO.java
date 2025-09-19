package br.com.meusistema.api.dtos;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProdutoRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 150)
        String nome,

        @NotNull(message = "Preço é obrigatório")
        @DecimalMin(value = "0.0", message = "Preço deve ser maior que zero")
        BigDecimal preco,

        String descricao,

        @NotNull(message = "Quantidade em estoque é obrigatório")
        @Min(value = 0, message = "Quantidade não pode ser negativa")
        Integer quantidadeEstoque,

        @NotNull(message = "Informe o fornecedor")
        Long fornecedor
) {
}
