package br.com.meusistema.api.service;

import br.com.meusistema.api.dtos.ProdutoRequestDTO;
import br.com.meusistema.api.dtos.ProdutoResponseDTO;
import br.com.meusistema.api.mapper.ProdutoMapper;
import br.com.meusistema.api.model.Fornecedor;
import br.com.meusistema.api.model.Produto;
import br.com.meusistema.api.repository.FornecedorRepository;
import br.com.meusistema.api.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl implements ProdutoService{
    private final ProdutoMapper produtoMapper;
    private final ProdutoRepository produtoRepository;
    private final FornecedorRepository fornecedorRepository;

    @Override
    public ProdutoResponseDTO criarProduto(ProdutoRequestDTO produtoRequestDTO) {
        Produto produto = produtoMapper.toEntity(produtoRequestDTO);
        produto.setFornecedor(buscarFornecedorPorId(produtoRequestDTO.fornecedor()));
        return produtoMapper.toDTO(produtoRepository.save(produto));
    }

    @Override
    public List<ProdutoResponseDTO> listarTodosProdutos() {
        return produtoRepository.findAll().stream()
                .map(produtoMapper::toDTO).toList();
    }

    @Override
    public ProdutoResponseDTO buscarProdutoPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Produto não encontrado"));
        return produtoMapper.toDTO(produto);
    }

    @Override
    public ProdutoResponseDTO atualizarProduto(Long id, ProdutoRequestDTO produtoRequestDTO) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Produto não encontrado"));
        produto.setNome(produtoRequestDTO.nome());
        produto.setPreco(produtoRequestDTO.preco());
        produto.setDescricao(produtoRequestDTO.descricao());
        produto.setQuantidadeEstoque(produtoRequestDTO.quantidadeEstoque());
        produto.setFornecedor(buscarFornecedorPorId(produtoRequestDTO.fornecedor()));

        return produtoMapper.toDTO(produtoRepository.save(produto));
    }

    @Override
    public void deletarProduto(Long id) {
        if(!produtoRepository.existsById(id)){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Produto não encontrado");
        }

        produtoRepository.deleteById(id);
    }

    private Fornecedor buscarFornecedorPorId(Long id){
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Fornecedor não encontrado"));
    }
}
