package br.com.meusistema.api.controller;

import br.com.meusistema.api.dtos.ProdutoRequestDTO;
import br.com.meusistema.api.dtos.ProdutoResponseDTO;
import br.com.meusistema.api.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/produtos")
@RequiredArgsConstructor
@RestController
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProdutoResponseDTO>criarProduto(
            @Valid @RequestBody ProdutoRequestDTO produtoRequestDTO){
        return ResponseEntity.status(201).body(produtoService.criarProduto(produtoRequestDTO));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<ProdutoResponseDTO>> listarTodosProdutos(){
        return ResponseEntity.ok(produtoService.listarTodosProdutos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ProdutoResponseDTO> buscarProdutoPorId(@PathVariable Long id){
        return ResponseEntity.ok(produtoService.buscarProdutoPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(
            @PathVariable Long id, @Valid @RequestBody ProdutoRequestDTO produtoRequestDTO){
        return ResponseEntity.ok(produtoService.atualizarProduto(id, produtoRequestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id){
        produtoService.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }
}
