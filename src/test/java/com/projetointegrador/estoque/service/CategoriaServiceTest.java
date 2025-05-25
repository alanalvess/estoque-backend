package com.projetointegrador.estoque.service;

import com.projetointegrador.estoque.dto.CategoriaDTO;
import com.projetointegrador.estoque.model.Categoria;
import com.projetointegrador.estoque.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoriaServiceTest {

    @InjectMocks
    private CategoriaService categoriaService;

    @Mock
    private CategoriaRepository categoriaRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarTodas_DeveRetornarListaDTO() {
        Categoria categoria = Categoria.builder()
                .id(1L)
                .nome("Bebidas")
                .produtos(new ArrayList<>())
                .build();

        when(categoriaRepository.findAll()).thenReturn(List.of(categoria));

        List<CategoriaDTO> resultado = categoriaService.listarTodas();

        assertEquals(1, resultado.size());
        assertEquals("Bebidas", resultado.get(0).nome());
    }

    @Test
    void buscarPorId_QuandoExiste_DeveRetornarDTO() {
        Categoria categoria = Categoria.builder()
                .id(1L)
                .nome("Hortifruti")
                .produtos(new ArrayList<>())
                .build();

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        CategoriaDTO dto = categoriaService.buscarPorId(1L);

        assertEquals("Hortifruti", dto.nome());
    }

    @Test
    void cadastrar_QuandoNomeNaoExiste_DeveSalvarENomear() {
        CategoriaDTO dto = new CategoriaDTO(null, "Limpeza");

        when(categoriaRepository.findByNomeIgnoreCase("Limpeza")).thenReturn(Optional.empty());
        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(inv -> {
            Categoria c = inv.getArgument(0);
            c.setId(99L);
            return c;
        });

        CategoriaDTO resultado = categoriaService.cadastrar(dto);

        assertNotNull(resultado);
        assertEquals("Limpeza", resultado.nome());
    }

    @Test
    void cadastrar_QuandoNomeJaExiste_DeveLancarExcecao() {
        Categoria categoriaExistente = Categoria.builder()
                .id(1L)
                .nome("Limpeza")
                .produtos(new ArrayList<>())
                .build();

        CategoriaDTO dto = new CategoriaDTO(null, "Limpeza");

        when(categoriaRepository.findByNomeIgnoreCase("Limpeza"))
                .thenReturn(Optional.of(categoriaExistente));

        assertThrows(IllegalArgumentException.class, () -> categoriaService.cadastrar(dto));
    }

    @Test
    void buscarPorNome_DeveRetornarCategoriasComNomeCorrespondente() {
        Categoria categoria = Categoria.builder()
                .id(1L)
                .nome("Doces")
                .produtos(new ArrayList<>())
                .build();

        when(categoriaRepository.findAllByNomeContainingIgnoreCase("Doce"))
                .thenReturn(List.of(categoria));

        List<CategoriaDTO> resultado = categoriaService.buscarPorNome("Doce");

        assertEquals(1, resultado.size());
        assertEquals("Doces", resultado.get(0).nome());
    }

    @Test
    void atualizar_QuandoIdExisteEValido_DeveAtualizarECategorizar() {
        Categoria categoria = Categoria.builder()
                .id(1L)
                .nome("Antigo")
                .produtos(new ArrayList<>())
                .build();

        CategoriaDTO dto = new CategoriaDTO(null, "Novo Nome");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<CategoriaDTO> resultado = categoriaService.atualizar(1L, dto);

        assertTrue(resultado.isPresent());
        assertEquals("Novo Nome", resultado.get().nome());
    }

    @Test
    void atualizar_QuandoNomeForNulo_DeveLancarExcecao() {
        Categoria categoria = Categoria.builder()
                .id(1L)
                .nome("Teste")
                .produtos(new ArrayList<>())
                .build();

        CategoriaDTO dto = new CategoriaDTO(null, null);

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        assertThrows(IllegalArgumentException.class, () -> categoriaService.atualizar(1L, dto));
    }

    @Test
    void atualizarAtributo_QuandoIdValido_DeveAtualizarApenasCamposPresentes() {
        Categoria categoria = Categoria.builder()
                .id(1L)
                .nome("Antigo")
                .produtos(new ArrayList<>())
                .build();

        CategoriaDTO dto = new CategoriaDTO(null, "Atualizado");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<CategoriaDTO> resultado = categoriaService.atualizarAtributo(1L, dto);

        assertTrue(resultado.isPresent());
        assertEquals("Atualizado", resultado.get().nome());
    }

    @Test
    void deletar_QuandoIdValido_DeveChamarDelete() {
        Categoria categoria = Categoria.builder()
                .id(1L)
                .nome("Excluir")
                .produtos(new ArrayList<>())
                .build();

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        categoriaService.deletar(1L);

        verify(categoriaRepository, times(1)).delete(categoria);
    }

    @Test
    void buscarPorId_QuandoNaoEncontrado_DeveLancarExcecao() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> categoriaService.buscarPorId(1L));
    }

}
