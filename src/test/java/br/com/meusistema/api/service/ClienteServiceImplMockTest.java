package br.com.meusistema.api.service;

import br.com.meusistema.api.dtos.ClienteRequestDTO;
import br.com.meusistema.api.dtos.ClienteResponseDTO;
import br.com.meusistema.api.dtos.EnderecoDTO;
import br.com.meusistema.api.mapper.ClienteMapper;
import br.com.meusistema.api.mapper.EnderecoMapper;
import br.com.meusistema.api.model.Cliente;
import br.com.meusistema.api.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceImplMockTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @Mock
    private EnderecoMapper enderecoMapper;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente;
    private ClienteRequestDTO clienteRequestDTO;
    private ClienteResponseDTO clienteResponseDTO;

    @BeforeEach
    void setup(){
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente Teste");
        cliente.setEmail("cliente@email.com");
        cliente.setCpf("71376034409");

        clienteRequestDTO = new ClienteRequestDTO(
                "Cliente Teste",
                "cliente@email.com",
                "71376034409",
                new EnderecoDTO(
                        "Rua do TESTE",
                        "12",
                        "Deposito 2",
                        "Varadouro",
                        "João Pessoa",
                        "Paraíba",
                        "Brasil",
                        "58010000")
        );

        clienteResponseDTO = new ClienteResponseDTO(
                1L,
                "Cliente Teste",
                "cliente@email.com",
                "71376034409",
                new EnderecoDTO(
                        "Rua do TESTE",
                        "12",
                        "Deposito 2",
                        "Varadouro",
                        "João Pessoa",
                        "Paraíba",
                        "Brasil",
                        "58010000")
        );
    }

    @Test
    void deveCriarClienteComSucesso(){
        when(clienteMapper.toEntity(clienteRequestDTO)).thenReturn(cliente);
        when(enderecoMapper.toEntity(clienteRequestDTO.endereco())).thenReturn(cliente.getEndereco());
        when(clienteRepository.save(cliente)).thenReturn(cliente);
        when(clienteMapper.toDTO(cliente)).thenReturn(clienteResponseDTO);

        ClienteResponseDTO resposta = clienteService.criarCliente(clienteRequestDTO);

        assertNotNull(resposta);
        assertEquals("Cliente Teste", resposta.nome());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void deveListarTodosClientes() {
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));
        when(clienteMapper.toDTO(cliente)).thenReturn(clienteResponseDTO);

        List<ClienteResponseDTO> lista = clienteService.listarTodosClientes();

        assertEquals(1, lista.size());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarClientePorId() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteMapper.toDTO(cliente)).thenReturn(clienteResponseDTO);

        ClienteResponseDTO resultado = clienteService.buscarClientePorId(1L);

        assertEquals("Cliente Teste", resultado.nome());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoExistirAoBuscarPorId(){
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () ->
                clienteService.buscarClientePorId(1L));
    }

    @Test
    void deveAtualizarClienteComSucesso() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(enderecoMapper.toEntity(clienteRequestDTO.endereco())).thenReturn(cliente.getEndereco());
        when(clienteRepository.save(cliente)).thenReturn(cliente);
        when(clienteMapper.toDTO(cliente)).thenReturn(clienteResponseDTO);

        ClienteResponseDTO atualizado = clienteService.atualizarCliente(
                1L, clienteRequestDTO);

        assertEquals("Cliente Teste", atualizado.nome());
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoExistirAoAtualizar() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () ->
                clienteService.atualizarCliente(1L, clienteRequestDTO));
    }

    @Test
    void deveDeletarClienteComSucesso() {
        when(clienteRepository.existsById(1L)).thenReturn(true);

        clienteService.deletarCliente(1L);
        verify(clienteRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoExistirAoDeletar() {
        when(clienteRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () ->
                clienteService.deletarCliente(1L));
    }
}
