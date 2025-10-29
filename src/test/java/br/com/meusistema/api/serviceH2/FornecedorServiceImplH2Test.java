package br.com.meusistema.api.serviceH2;

import br.com.meusistema.api.MeuSistemaApplication;
import br.com.meusistema.api.dtos.EnderecoDTO;
import br.com.meusistema.api.dtos.FornecedorRequestDTO;
import br.com.meusistema.api.dtos.FornecedorResponseDTO;
import br.com.meusistema.api.enums.TipoFornecedorEnum;
import br.com.meusistema.api.repository.FornecedorRepository;
import br.com.meusistema.api.service.FornecedorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(
        classes = MeuSistemaApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FornecedorServiceImplH2Test {

    @Autowired
    private FornecedorService fornecedorService;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    private FornecedorRequestDTO fornecedorRequestDTO;

    @BeforeEach
    void setup(){
        fornecedorRepository.deleteAll();

        fornecedorRequestDTO = new FornecedorRequestDTO(
                "Fornecedor Teste",
                "teste@email.com",
                "12345678000199",
                TipoFornecedorEnum.COMUM,
                new EnderecoDTO(
                        "Rua A",
                        "123",
                        "Apto 01",
                        "Centro",
                        "João Pessoa",
                        "PB",
                        "Brasil",
                        "58000-000"
                )
        );
    }

    @Test
    void deveCriarFornecedorNoBancoH2() {
        FornecedorResponseDTO response = fornecedorService.criarFornecedor(fornecedorRequestDTO);

        assertNotNull(response);
        assertEquals("Fornecedor Teste", response.nomeFantasia());
        assertEquals(1, fornecedorRepository.count());
    }

    @Test
    void deveListarFornecedor() {
        fornecedorService.criarFornecedor(fornecedorRequestDTO);

        List<FornecedorResponseDTO> lista = fornecedorService.listarTodosFornecedores();

        assertEquals(1, lista.size());
        assertEquals("Fornecedor Teste", lista.get(0).nomeFantasia());
    }

    @Test
    void deveBuscarFornecedorPorId() {
        FornecedorResponseDTO criado = fornecedorService.criarFornecedor(fornecedorRequestDTO);
        FornecedorResponseDTO buscado = fornecedorService.buscarFornecedorPorId(criado.id());

        assertEquals(criado.id(), buscado.id());
    }

    @Test
    void deveLancarExcecaoAoBuscarFornecedorInexistente() {
        assertThrows(ResponseStatusException.class, () -> fornecedorService.buscarFornecedorPorId(999L));
    }

    @Test
    void deveAtualizarFornecedor() {
        FornecedorResponseDTO criado = fornecedorService.criarFornecedor(fornecedorRequestDTO);

        FornecedorRequestDTO atualizadoDTO = new FornecedorRequestDTO(
                "Fornecedor Atualizado",
                "atualizado@email.com",
                "12345678000199",
                TipoFornecedorEnum.PREMIUM,
                fornecedorRequestDTO.endereco()
        );

        FornecedorResponseDTO atualizado = fornecedorService.
                atualizarFornecedor(criado.id(), atualizadoDTO);

        assertEquals("Fornecedor Atualizado", atualizado.nomeFantasia());
        assertEquals(TipoFornecedorEnum.PREMIUM, atualizado.tipoFornecedor());
    }

    @Test
    void deveDeletarFornecedor() {
        FornecedorResponseDTO criado = fornecedorService.criarFornecedor(fornecedorRequestDTO);

        fornecedorService.deletarFornecedor(criado.id());

        assertEquals(0, fornecedorRepository.count());
    }

    @Test
    void deveLancarExcecaoAoDeletarFornecedorInexistente() {
        assertThrows(ResponseStatusException.class, () ->
                fornecedorService.deletarFornecedor(999L));
    }

}
