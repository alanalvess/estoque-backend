package com.projetointegrador.estoque.service;

import com.projetointegrador.estoque.dto.FornecedorDTO;
import com.projetointegrador.estoque.exeption.FornecedorDuplicadoException;
import com.projetointegrador.estoque.model.Fornecedor;
import com.projetointegrador.estoque.repository.FornecedorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FornecedorServiceTest {

    @InjectMocks
    private FornecedorService fornecedorService;

    @Mock
    private FornecedorRepository fornecedorRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarTodos_DeveRetornarListaDeDTOs() {
        Fornecedor fornecedor = Fornecedor.builder()
                .id(1L)
                .nome("Fornecedor A")
                .cnpj("12345678900001")
                .email("email@teste.com")
                .telefone("1111-1111")
                .endereco("Rua X")
                .build();

        when(fornecedorRepository.findAll()).thenReturn(List.of(fornecedor));

        List<FornecedorDTO> resultado = fornecedorService.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals("Fornecedor A", resultado.get(0).nome());
    }

    @Test
    void buscarPorId_QuandoExiste_DeveRetornarDTO() {
        Fornecedor fornecedor = Fornecedor.builder()
                .id(1L)
                .nome("Fornecedor B")
                .cnpj("22222222222222")
                .email("email@teste.com")
                .telefone("2222-2222")
                .endereco("Rua Y")
                .build();

        when(fornecedorRepository.findById(1L)).thenReturn(Optional.of(fornecedor));

        FornecedorDTO resultado = fornecedorService.buscarPorId(1L);

        assertEquals("Fornecedor B", resultado.nome());
    }

    @Test
    void buscarPorId_QuandoNaoExiste_DeveLancarExcecao() {
        when(fornecedorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> fornecedorService.buscarPorId(99L));
    }

    @Test
    void buscarPorNome_DeveRetornarFornecedoresCorrespondentes() {
        Fornecedor fornecedor = Fornecedor.builder()
                .id(1L)
                .nome("Fornecedor C")
                .cnpj("33333333333333")
                .email("email@teste.com")
                .telefone("3333-3333")
                .endereco("Rua Z")
                .build();

        when(fornecedorRepository.findAllByNomeContainingIgnoreCase("Fornecedor"))
                .thenReturn(List.of(fornecedor));

        List<FornecedorDTO> resultado = fornecedorService.buscarPorNome("Fornecedor");

        assertEquals(1, resultado.size());
        assertEquals("Fornecedor C", resultado.get(0).nome());
    }

    @Test
    void buscarPorCNPJ_DeveRetornarFornecedoresCorrespondentes() {
        Fornecedor fornecedor = Fornecedor.builder()
                .id(1L)
                .nome("Fornecedor D")
                .cnpj("44444444444444")
                .email("email@teste.com")
                .telefone("4444-4444")
                .endereco("Rua W")
                .build();

        when(fornecedorRepository.findAllByCnpjContainingIgnoreCase("4444"))
                .thenReturn(List.of(fornecedor));

        List<FornecedorDTO> resultado = fornecedorService.buscarPorCNPJ("4444");

        assertEquals(1, resultado.size());
        assertEquals("44444444444444", resultado.get(0).cnpj());
    }

    @Test
    void cadastrar_QuandoNomeNaoExiste_DeveCadastrarFornecedor() {
        FornecedorDTO dto = new FornecedorDTO(null, "Novo Fornecedor", "55555555555555", "novo@email.com", "5555-5555", "Rua Nova");

        when(fornecedorRepository.findByNomeIgnoreCase("Novo Fornecedor")).thenReturn(Optional.empty());
        when(fornecedorRepository.save(any(Fornecedor.class))).thenAnswer(invocation -> {
            Fornecedor f = invocation.getArgument(0);
            f.setId(1L);
            return f; // caso precise setar o ID no retorno
        });

        FornecedorDTO resultado = fornecedorService.cadastrar(dto);

        assertNotNull(resultado);
        assertEquals("Novo Fornecedor", resultado.nome());
    }

    @Test
    void cadastrar_QuandoNomeJaExiste_DeveLancarExcecao() {
        Fornecedor fornecedor = Fornecedor.builder()
                .id(1L)
                .nome("Fornecedor Existente")
                .cnpj("66666666666666")
                .email("existente@email.com")
                .telefone("6666-6666")
                .endereco("Rua Existente")
                .build();

        FornecedorDTO dto = new FornecedorDTO(null, "Fornecedor Existente", "66666666666666", "existente@email.com", "6666-6666", "Rua Existente");

        when(fornecedorRepository.findByNomeIgnoreCase("Fornecedor Existente"))
                .thenReturn(Optional.of(fornecedor));

        assertThrows(FornecedorDuplicadoException.class, () -> fornecedorService.cadastrar(dto));
    }

    @Test
    void atualizar_QuandoIdExiste_DeveAtualizarFornecedor() {
        Fornecedor fornecedor = Fornecedor.builder()
                .id(1L)
                .nome("Fornecedor Antigo")
                .cnpj("77777777777777")
                .email("antigo@email.com")
                .telefone("7777-7777")
                .endereco("Rua Antiga")
                .build();

        FornecedorDTO dto = new FornecedorDTO(null, "Fornecedor Atualizado", "77777777777777", "novo@email.com", "8888-8888", "Rua Nova");

        when(fornecedorRepository.findById(1L)).thenReturn(Optional.of(fornecedor));
        when(fornecedorRepository.save(any(Fornecedor.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<FornecedorDTO> resultado = fornecedorService.atualizar(1L, dto);

        assertTrue(resultado.isPresent());
        assertEquals("Fornecedor Atualizado", resultado.get().nome());
        assertEquals("novo@email.com", resultado.get().email());
    }

    @Test
    void atualizar_QuandoNomeForNulo_DeveLancarExcecao() {
        Fornecedor fornecedor = Fornecedor.builder()
                .id(1L)
                .nome("Fornecedor")
                .cnpj("88888888888888")
                .email("email@email.com")
                .telefone("9999-9999")
                .endereco("Rua Antiga")
                .build();

        FornecedorDTO dto = new FornecedorDTO(null, null, "88888888888888", "email@email.com", "9999-9999", "Rua Antiga");

        when(fornecedorRepository.findById(1L)).thenReturn(Optional.of(fornecedor));

        assertThrows(IllegalArgumentException.class, () -> fornecedorService.atualizar(1L, dto));
    }

    @Test
    void atualizarAtributo_DeveAtualizarApenasCamposPresentes() {
        Fornecedor fornecedor = Fornecedor.builder()
                .id(1L)
                .nome("Fornecedor")
                .cnpj("99999999999999")
                .email("email@antigo.com")
                .telefone("0000-0000")
                .endereco("Rua Velha")
                .build();

        FornecedorDTO dto = new FornecedorDTO(null, "Atualizado", null, "novo@email.com", null, null);

        when(fornecedorRepository.findById(1L)).thenReturn(Optional.of(fornecedor));
        when(fornecedorRepository.save(any(Fornecedor.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<FornecedorDTO> resultado = fornecedorService.atualizarAtributo(1L, dto);

        assertTrue(resultado.isPresent());
        assertEquals("Atualizado", resultado.get().nome());
        assertEquals("novo@email.com", resultado.get().email());
    }

    @Test
    void deletar_QuandoIdValido_DeveRemoverFornecedor() {
        Fornecedor fornecedor = Fornecedor.builder()
                .id(1L)
                .nome("Excluir")
                .cnpj("10101010101010")
                .email("excluir@email.com")
                .telefone("1010-1010")
                .endereco("Rua X")
                .build();

        when(fornecedorRepository.findById(1L)).thenReturn(Optional.of(fornecedor));

        fornecedorService.deletar(1L);

        verify(fornecedorRepository, times(1)).delete(fornecedor);
    }
}
