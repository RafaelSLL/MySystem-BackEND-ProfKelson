package br.com.meusistema.api.controller;

import br.com.meusistema.api.dtos.FornecedorRequestDTO;
import br.com.meusistema.api.dtos.FornecedorResponseDTO;
import br.com.meusistema.api.service.FornecedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/fornecedores")
@RequiredArgsConstructor
@RestController
public class FornecedorController {

    private final FornecedorService fornecedorService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FornecedorResponseDTO> criarFornecedor(
            @Valid @RequestBody FornecedorRequestDTO fornecedorRequestDTO){

        return ResponseEntity.status(201).body(
                fornecedorService.criarFornecedor(fornecedorRequestDTO));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<FornecedorResponseDTO>> listarTodosFornecedores(){
        return ResponseEntity.ok(fornecedorService.listarTodosFornecedores());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<FornecedorResponseDTO> buscarFornecedorPorId(@PathVariable Long id){
        return ResponseEntity.ok(fornecedorService.buscarFornecedorPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FornecedorResponseDTO> atualizarFornecedor(
            @PathVariable Long id, @RequestBody FornecedorRequestDTO fornecedorRequestDTO){
        return ResponseEntity.ok(fornecedorService.atualizarFornecedor(id, fornecedorRequestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarFornecedor(@PathVariable Long id){
        fornecedorService.deletarFornecedor(id);
        return ResponseEntity.noContent().build();
    }


}
