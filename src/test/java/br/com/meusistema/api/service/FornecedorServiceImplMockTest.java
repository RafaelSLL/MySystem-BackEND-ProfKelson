package br.com.meusistema.api.service;

import br.com.meusistema.api.dtos.EnderecoDTO;
import br.com.meusistema.api.dtos.FornecedorRequestDTO;
import br.com.meusistema.api.dtos.FornecedorResponseDTO;
import br.com.meusistema.api.enums.TipoFornecedorEnum;
import br.com.meusistema.api.mapper.EnderecoMapper;
import br.com.meusistema.api.mapper.FornecedorMapper;
import br.com.meusistema.api.model.Fornecedor;
import br.com.meusistema.api.repository.FornecedorRepository;
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
public class FornecedorServiceImplMockTest {

    @Mock
    private FornecedorRepository fornecedorRepository;

    @Mock
    private FornecedorMapper fornecedorMapper;

    @Mock
    private EnderecoMapper enderecoMapper;

    @InjectMocks
    private FornecedorServiceImpl fornecedorService;

    private Fornecedor fornecedor;
    private FornecedorRequestDTO fornecedorRequestDTO;
    private FornecedorResponseDTO fornecedorResponseDTO;

    @BeforeEach
    void setup(){
        fornecedor = new Fornecedor();
        fornecedor.setId(1L);
        fornecedor.setNomeFantasia("Fornecedor Teste");
        fornecedor.setEmail("teste@email.com");
        fornecedor.setCnpj("12345678000199");
        fornecedor.setTipoFornecedor(TipoFornecedorEnum.PREMIUM);

        fornecedorRequestDTO = new FornecedorRequestDTO(
                "Fornecedor Teste",
                "teste@email.com",
                "12345678000199",
                TipoFornecedorEnum.PREMIUM,
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

        fornecedorResponseDTO = new FornecedorResponseDTO(
                1L,
                "Fornecedor Teste",
                "teste@email.com",
                "12345678000199",
                TipoFornecedorEnum.PREMIUM,
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
    void deveCriarFornecedorComSucesso() {
        when(fornecedorMapper.toEntity(fornecedorRequestDTO)).thenReturn(fornecedor);
        when(enderecoMapper.toEntity(fornecedorRequestDTO.endereco())).thenReturn(fornecedor.getEndereco());
        when(fornecedorRepository.save(fornecedor)).thenReturn(fornecedor);
        when(fornecedorMapper.toDTO(fornecedor)).thenReturn(fornecedorResponseDTO);

        FornecedorResponseDTO resposta = fornecedorService.criarFornecedor(fornecedorRequestDTO);

        assertNotNull(resposta);
        assertEquals("Fornecedor Teste", resposta.nomeFantasia());
        verify(fornecedorRepository, times(1)).save(any(Fornecedor.class));
    }

    @Test
    void deveListarTodosFornecedores() {
        when(fornecedorRepository.findAll()).thenReturn(List.of(fornecedor));
        when(fornecedorMapper.toDTO(fornecedor)).thenReturn(fornecedorResponseDTO);

        List<FornecedorResponseDTO> lista = fornecedorService.listarTodosFornecedores();

        assertEquals(1, lista.size());
        verify(fornecedorRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarFornecedorPorId() {
        when(fornecedorRepository.findById(1L)).thenReturn(Optional.of(fornecedor));
        when(fornecedorMapper.toDTO(fornecedor)).thenReturn(fornecedorResponseDTO);

        FornecedorResponseDTO resultado = fornecedorService.buscarFornecedorPorId(1L);

        assertEquals("Fornecedor Teste", resultado.nomeFantasia());
        verify(fornecedorRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoFornecedorNaoExistirAoBuscarPorId(){
        when(fornecedorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () ->
                fornecedorService.buscarFornecedorPorId(1L));
    }

    @Test
    void deveAtualizarFornecedorComSucesso() {
        when(fornecedorRepository.findById(1L)).thenReturn(Optional.of(fornecedor));
        when(enderecoMapper.toEntity(fornecedorRequestDTO.endereco())).thenReturn(fornecedor.getEndereco());
        when(fornecedorRepository.save(fornecedor)).thenReturn(fornecedor);
        when(fornecedorMapper.toDTO(fornecedor)).thenReturn(fornecedorResponseDTO);

        FornecedorResponseDTO atualizado = fornecedorService.
                atualizarFornecedor(1L, fornecedorRequestDTO);

        assertEquals("Fornecedor Teste", atualizado.nomeFantasia());
        verify(fornecedorRepository, times(1)).save(fornecedor);
    }

    @Test
    void deveLancarExcecaoQuandoFornecedorNaoExistirAoAtualizar() {
        when(fornecedorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () ->
                fornecedorService.atualizarFornecedor(1L, fornecedorRequestDTO));
    }

    @Test
    void deveDeletarFornecedorComSucesso() {
        when(fornecedorRepository.existsById(1L)).thenReturn(true);

        fornecedorService.deletarFornecedor(1L);

        verify(fornecedorRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoFornecedorNaoExistirAoDeletar() {
        when(fornecedorRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResponseStatusException.class,
                () -> fornecedorService.deletarFornecedor(1L));
    }
}
