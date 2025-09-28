package br.com.meusistema.api.controller;

import br.com.meusistema.api.dtos.ClienteRequestDTO;
import br.com.meusistema.api.dtos.ClienteResponseDTO;
import br.com.meusistema.api.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/clientes")
@RequiredArgsConstructor
@RestController
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteResponseDTO> criarCliente(
            @Valid @RequestBody ClienteRequestDTO clienteRequestDTO){

        return ResponseEntity.status(201).body(
                clienteService.criarCliente(clienteRequestDTO));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<ClienteResponseDTO>> listarTodosClientes(){
        return ResponseEntity.ok(clienteService.listarTodosClientes());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ClienteResponseDTO> buscarClientePorId(@PathVariable Long id){
        return ResponseEntity.ok(clienteService.buscarClientePorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteResponseDTO> atualizarCliente(
            @PathVariable Long id, @Valid @RequestBody ClienteRequestDTO clienteRequestDTO){
        return ResponseEntity.ok(clienteService.atualizarCliente(id, clienteRequestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id){
        clienteService.deletarCliente(id);
        return ResponseEntity.noContent().build();
    }
}
