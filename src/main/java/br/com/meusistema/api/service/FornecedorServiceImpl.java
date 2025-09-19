package br.com.meusistema.api.service;

import br.com.meusistema.api.dtos.FornecedorRequestDTO;
import br.com.meusistema.api.dtos.FornecedorResponseDTO;
import br.com.meusistema.api.mapper.EnderecoMapper;
import br.com.meusistema.api.mapper.FornecedorMapper;
import br.com.meusistema.api.model.Fornecedor;
import br.com.meusistema.api.repository.FornecedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FornecedorServiceImpl implements FornecedorService {

    private final FornecedorMapper fornecedorMapper;
    private final FornecedorRepository fornecedorRepository;
    private final EnderecoMapper enderecoMapper;

    @Override
    public FornecedorResponseDTO criarFornecedor(FornecedorRequestDTO fornecedorRequestDTO){
        Fornecedor fornecedor = fornecedorMapper.toEntity(fornecedorRequestDTO);
        fornecedor.setEndereco(enderecoMapper.toEntity(fornecedorRequestDTO.endereco()));

        return fornecedorMapper.toDTO(fornecedorRepository.save(fornecedor));
    }

    @Override
    public List<FornecedorResponseDTO> listarTodosFornecedores(){
        return fornecedorRepository.findAll().stream()
                .map(fornecedorMapper::toDTO).toList();
    }

    @Override
    public FornecedorResponseDTO buscarFornecedorPorId(Long id){
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Fornecedor não encontrado"));
        return fornecedorMapper.toDTO(fornecedor);
    }

    @Override
    public FornecedorResponseDTO atualizarFornecedor(Long id, FornecedorRequestDTO fornecedorRequestDTO){
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Fornecedor não encontrado"));
        fornecedor.setNomeFantasia(fornecedorRequestDTO.nomeFantasia());
        fornecedor.setEmail(fornecedorRequestDTO.email());
        fornecedor.setCnpj(fornecedorRequestDTO.cnpj());
        fornecedor.setTipoFornecedor(fornecedorRequestDTO.tipoFornecedor());
        fornecedor.setEndereco(enderecoMapper.toEntity(fornecedorRequestDTO.endereco()));

        return fornecedorMapper.toDTO(fornecedorRepository.save(fornecedor));
    }

    @Override
    public void deletarFornecedor(Long id) {
        if(!fornecedorRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor não encontrado");
        }

        fornecedorRepository.deleteById(id);
    }
}
