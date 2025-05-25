package com.projetointegrador.estoque.service;

import com.projetointegrador.estoque.dto.MarcaDTO;
import com.projetointegrador.estoque.model.Marca;
import com.projetointegrador.estoque.repository.MarcaRepository;
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

public class MarcaServiceTest {

    @InjectMocks
    private MarcaService marcaService;

    @Mock
    private MarcaRepository marcaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarTodas_DeveRetornarListaDeDTOs() {
        Marca marca = new Marca();
        marca.setId(1L);
        marca.setNome("Nike");

        when(marcaRepository.findAll()).thenReturn(List.of(marca));

        List<MarcaDTO> resultado = marcaService.listarTodas();

        assertEquals(1, resultado.size());
        assertEquals("Nike", resultado.get(0).nome());
    }

    @Test
    void buscarPorId_QuandoExiste_DeveRetornarDTO() {
        Marca marca = new Marca();
        marca.setId(1L);
        marca.setNome("Adidas");

        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));

        MarcaDTO dto = marcaService.buscarPorId(1L);

        assertEquals("Adidas", dto.nome());
    }

    @Test
    void buscarPorId_QuandoNaoExiste_DeveLancarExcecao() {
        when(marcaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> marcaService.buscarPorId(1L));
    }

    @Test
    void buscarPorNome_DeveRetornarMarcasCorrespondentes() {
        Marca marca = new Marca();
        marca.setId(1L);
        marca.setNome("Reebok");

        when(marcaRepository.findAllByNomeContainingIgnoreCase("Ree")).thenReturn(List.of(marca));

        List<MarcaDTO> resultado = marcaService.buscarPorNome("Ree");

        assertEquals(1, resultado.size());
        assertEquals("Reebok", resultado.get(0).nome());
    }

    @Test
    void cadastrar_QuandoNomeNaoExiste_DeveSalvar() {
        MarcaDTO dto = new MarcaDTO(null, "Puma");

        when(marcaRepository.findByNomeIgnoreCase("Puma")).thenReturn(Optional.empty());
        when(marcaRepository.save(any(Marca.class))).thenAnswer(inv -> {
            Marca m = inv.getArgument(0);
            m.setId(10L);
            return m;
        });

        MarcaDTO resultado = marcaService.cadastrar(dto);

        assertNotNull(resultado);
        assertEquals("Puma", resultado.nome());
    }

    @Test
    void cadastrar_QuandoNomeJaExiste_DeveLancarExcecao() {
        Marca marcaExistente = new Marca();
        marcaExistente.setId(1L);
        marcaExistente.setNome("Puma");

        MarcaDTO dto = new MarcaDTO(null, "Puma");

        when(marcaRepository.findByNomeIgnoreCase("Puma")).thenReturn(Optional.of(marcaExistente));

        assertThrows(IllegalArgumentException.class, () -> marcaService.cadastrar(dto));
    }

    @Test
    void atualizar_QuandoIdExisteEValido_DeveAtualizar() {
        Marca marca = new Marca();
        marca.setId(1L);
        marca.setNome("Antiga");

        MarcaDTO dto = new MarcaDTO(null, "Nova");

        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));
        when(marcaRepository.save(any(Marca.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<MarcaDTO> resultado = marcaService.atualizar(1L, dto);

        assertTrue(resultado.isPresent());
        assertEquals("Nova", resultado.get().nome());
    }

    @Test
    void atualizar_QuandoNomeForNulo_DeveLancarExcecao() {
        Marca marca = new Marca();
        marca.setId(1L);
        marca.setNome("Teste");

        MarcaDTO dto = new MarcaDTO(null, null);

        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));

        assertThrows(IllegalArgumentException.class, () -> marcaService.atualizar(1L, dto));
    }

    @Test
    void atualizarAtributo_QuandoIdValido_DeveAtualizarCamposPresentes() {
        Marca marca = new Marca();
        marca.setId(1L);
        marca.setNome("Original");

        MarcaDTO dto = new MarcaDTO(null, "Atualizada");

        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));
        when(marcaRepository.save(any(Marca.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<MarcaDTO> resultado = marcaService.atualizarAtributo(1L, dto);

        assertTrue(resultado.isPresent());
        assertEquals("Atualizada", resultado.get().nome());
    }

    @Test
    void deletar_QuandoIdValido_DeveChamarDelete() {
        Marca marca = new Marca();
        marca.setId(1L);
        marca.setNome("Excluir");

        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));

        marcaService.deletar(1L);

        verify(marcaRepository, times(1)).delete(marca);
    }
}