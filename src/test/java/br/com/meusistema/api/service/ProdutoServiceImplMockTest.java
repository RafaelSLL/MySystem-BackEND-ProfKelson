package br.com.meusistema.api.service;

import br.com.meusistema.api.dtos.EnderecoDTO;
import br.com.meusistema.api.dtos.FornecedorResponseDTO;
import br.com.meusistema.api.dtos.ProdutoRequestDTO;
import br.com.meusistema.api.dtos.ProdutoResponseDTO;
import br.com.meusistema.api.enums.TipoFornecedorEnum;
import br.com.meusistema.api.mapper.ProdutoMapper;
import br.com.meusistema.api.model.Fornecedor;
import br.com.meusistema.api.model.Produto;
import br.com.meusistema.api.repository.FornecedorRepository;
import br.com.meusistema.api.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ProdutoServiceImplMockTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ProdutoMapper produtoMapper;

    @Mock
    private FornecedorRepository fornecedorRepository;

    @InjectMocks
    private ProdutoServiceImpl produtoService;

    private Produto produto;
    private Fornecedor fornecedor;
    private ProdutoRequestDTO produtoRequestDTO;
    private ProdutoResponseDTO produtoResponseDTO;

    @BeforeEach
    void setup(){
        fornecedor = new Fornecedor();
        fornecedor.setId(1L);
        fornecedor.setNomeFantasia("Fornecedor Teste");
        fornecedor.setEmail("teste@email.com");
        fornecedor.setCnpj("12345678000199");
        fornecedor.setTipoFornecedor(TipoFornecedorEnum.PREMIUM);

        produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");
        produto.setDescricao("Produto para teste");
        produto.setPreco(BigDecimal.valueOf(13.40));
        produto.setQuantidadeEstoque(2);
        produto.setFornecedor(fornecedor);

        produtoRequestDTO = new ProdutoRequestDTO(
                "Produto Teste",
                BigDecimal.valueOf(13.40),
                "Produto para teste",
                2,
                1L
        );

        produtoResponseDTO = new ProdutoResponseDTO(
                1L,
                "Produto Teste",
                BigDecimal.valueOf(13.40),
                "Produto para teste",
                2,
                new FornecedorResponseDTO(
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
                )
        );
    }

    @Test
    void deveCriarProdutoComSucesso() {
        when(produtoMapper.toEntity(produtoRequestDTO)).thenReturn(produto);
        when(fornecedorRepository.findById(1L)).thenReturn(Optional.of(fornecedor));
        when(produtoRepository.save(produto)).thenReturn(produto);
        when(produtoMapper.toDTO(produto)).thenReturn(produtoResponseDTO);

        ProdutoResponseDTO resposta = produtoService.criarProduto(produtoRequestDTO);

        assertNotNull(resposta);
        assertEquals("Produto Teste", resposta.nome());
        verify(produtoRepository, times(1)).save(produto);
        verify(fornecedorRepository, times(1)).findById(1L);
    }

    @Test
    void deveListarTodosProdutos(){
        when(produtoRepository.findAll()).thenReturn(List.of(produto));
        when(produtoMapper.toDTO(produto)).thenReturn(produtoResponseDTO);

        List<ProdutoResponseDTO> lista = produtoService.listarTodosProdutos();

        assertEquals(1, lista.size());
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarProdutosPorId() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoMapper.toDTO(produto)).thenReturn(produtoResponseDTO);

        ProdutoResponseDTO resultado = produtoService.buscarProdutoPorId(1L);

        assertEquals("Produto Teste", resultado.nome());
        verify(produtoRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExistirAoBuscarPorId(){
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () ->
                produtoService.buscarProdutoPorId(1L));
    }

    @Test
    void deveAtualizarProdutoComSucesso() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(fornecedorRepository.findById(1L)).thenReturn(Optional.of(fornecedor));
        when(produtoRepository.save(produto)).thenReturn(produto);
        when(produtoMapper.toDTO(produto)).thenReturn(produtoResponseDTO);

        ProdutoResponseDTO atualizado = produtoService.atualizarProduto(1L, produtoRequestDTO);

        assertEquals("Produto Teste", atualizado.nome());
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExistirAoAtualizar() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () ->
                produtoService.atualizarProduto(1L, produtoRequestDTO));
    }

    @Test
    void deveLancarExcecaoQuandoFornecedorDoProdutoNaoExistirAoAtualizar() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(fornecedorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () ->
                produtoService.atualizarProduto(1L, produtoRequestDTO));
    }

    @Test
    void deveDeletarProdutoComSucesso() {
        when(produtoRepository.existsById(1L)).thenReturn(true);

        produtoService.deletarProduto(1L);
        verify(produtoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExistirAoDeletar() {
        when(produtoRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () ->
                produtoService.deletarProduto(1L));
    }

}

