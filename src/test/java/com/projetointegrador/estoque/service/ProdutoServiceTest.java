package com.projetointegrador.estoque.service;

import com.projetointegrador.estoque.dto.CategoriaDTO;
import com.projetointegrador.estoque.dto.FornecedorDTO;
import com.projetointegrador.estoque.dto.MarcaDTO;
import com.projetointegrador.estoque.dto.ProdutoDTO;
import com.projetointegrador.estoque.enums.UnidadeMedida;
import com.projetointegrador.estoque.model.Categoria;
import com.projetointegrador.estoque.model.Fornecedor;
import com.projetointegrador.estoque.model.Marca;
import com.projetointegrador.estoque.model.Produto;
import com.projetointegrador.estoque.repository.CategoriaRepository;
import com.projetointegrador.estoque.repository.FornecedorRepository;
import com.projetointegrador.estoque.repository.MarcaRepository;
import com.projetointegrador.estoque.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private FornecedorRepository fornecedorRepository;

    @Mock
    private MarcaRepository marcaRepository;

    @Mock
    private CategoriaService categoriaService;

    @Mock
    private MarcaService marcaService;

    @Mock
    private FornecedorService fornecedorService;

    private Produto produto;
    private ProdutoDTO produtoDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // PADRÃO

        produto = Produto.builder()
                .id(1L)
                .nome("Produto Teste")
                .descricao("Descrição")
                .valor(BigDecimal.valueOf(100))
                .quantidade(10)
                .unidadeMedida(UnidadeMedida.UNIDADE)
                .codigo("ABC123")
                .estoqueMinimo(1)
                .estoqueMaximo(100)
                .disponivel(true)
                .dataEntrada(LocalDate.now())
                .categoria(Categoria.builder()
                        .id(1L)
                        .nome("Hortifruti")
                        .build())
                .marca(Marca.builder()
                        .id(1L)
                        .nome("Marca")
                        .build())
                .fornecedor(Fornecedor.builder()
                        .id(1L)
                        .nome("Fornecedor Existente")
                        .cnpj("66666666666666")
                        .email("existente@email.com")
                        .telefone("6666-6666")
                        .endereco("Rua Existente")
                        .build())
                .build();

        produtoDTO = new ProdutoDTO(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getValor(),
                produto.getQuantidade(),
                produto.isDisponivel(),
                produto.getUnidadeMedida(),
                produto.getCodigo(),
                produto.getEstoqueMinimo(),
                produto.getEstoqueMaximo(),
                produto.getDataValidade(),
                produto.getDataEntrada(),
                produto.getDataSaida(),
                produto.getCategoria(),
                produto.getMarca(),
                produto.getFornecedor()
        );
    }

    @Test
    void buscarPorId_QuandoIdValido_DeveRetornarDTO() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        ProdutoDTO resultado = produtoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(produto.getNome(), resultado.nome());
    }

    @Test
    void buscarPorId_QuandoNaoEncontrado_DeveLancarExcecao() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> produtoService.buscarPorId(1L));
    }

    @Test
    void cadastrar_DeveRetornarProdutoDTO_QuandoDadosValidos() {

        Categoria categoria = Categoria.builder()
                .id(1L)
                .nome("Hortifruti")
                .build();
        Marca marca = Marca.builder()
                .id(1L)
                .nome("Marca")
                .build();
        Fornecedor fornecedor = Fornecedor.builder()
                .id(1L)
                .nome("Fornecedor Existente")
                .cnpj("66666666666666")
                .email("existente@email.com")
                .telefone("6666-6666")
                .endereco("Rua Existente")
                .build();

        ProdutoDTO dto = new ProdutoDTO(
                null,
                "Banana",
                "Banana nanica",
                new BigDecimal("2.50"),
                10,
                true,
                UnidadeMedida.UNIDADE,
                "BNN001",
                5,
                50,
                LocalDate.of(2025, 12, 31),
                LocalDate.of(2025, 1, 1),
                null,
                categoria,
                marca,
                fornecedor
        );


        Produto produtoSalvo = new Produto(dto);
        produtoSalvo.setId(1L);
        produtoSalvo.setCategoria(categoria);
        produtoSalvo.setMarca(marca);
        produtoSalvo.setFornecedor(fornecedor);

        when(produtoRepository.findByNomeIgnoreCase("Banana")).thenReturn(Optional.empty());
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));
        when(fornecedorRepository.findById(1L)).thenReturn(Optional.of(fornecedor));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoSalvo);

        ProdutoDTO resultado = produtoService.cadastrar(dto);

        assertNotNull(resultado);
        assertEquals("Banana", resultado.nome());
        assertEquals("Hortifruti", resultado.categoria().getNome());
    }


    @Test
    void atualizar_QuandoIdExiste_DeveRetornarProdutoAtualizado() {

        Categoria categoria = Categoria.builder()
                .id(1L)
                .nome("Hortifruti")
                .build();
        Marca marca = Marca.builder()
                .id(1L)
                .nome("Marca")
                .build();
        Fornecedor fornecedor = Fornecedor.builder()
                .id(1L)
                .nome("Fornecedor Existente")
                .cnpj("66666666666666")
                .email("existente@email.com")
                .telefone("6666-6666")
                .endereco("Rua Existente")
                .build();

        Produto atualizado = Produto.builder()
                .id(1L)
                .nome("Atualizado")
                .descricao("Descrição original")
                .valor(BigDecimal.TEN)
                .quantidade(10)
                .disponivel(true)
                .unidadeMedida(UnidadeMedida.UNIDADE)
                .codigo("ABC123")
                .estoqueMinimo(5)
                .estoqueMaximo(20)
                .dataValidade(LocalDate.of(2025, 12, 31))
                .dataEntrada(LocalDate.now())
                .dataSaida(LocalDate.now())
                .categoria(categoria)
                .marca(marca)
                .fornecedor(fornecedor)
                .build();

        ProdutoDTO dto = new ProdutoDTO(
                null,
                "Atualizado",
                atualizado.getDescricao(),
                atualizado.getValor(),
                atualizado.getQuantidade(),
                atualizado.isDisponivel(),
                atualizado.getUnidadeMedida(),
                atualizado.getCodigo(),
                atualizado.getEstoqueMinimo(),
                atualizado.getEstoqueMaximo(),
                atualizado.getDataValidade(),
                atualizado.getDataEntrada(),
                atualizado.getDataSaida(),
                categoria,
                marca,
                fornecedor
        );

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));
        when(fornecedorRepository.findById(1L)).thenReturn(Optional.of(fornecedor));
        when(produtoRepository.save(any(Produto.class))).thenReturn(atualizado);

        Optional<ProdutoDTO> resultado = produtoService.atualizar(1L, dto);

        assertEquals("Atualizado", resultado.get().nome());
    }

    @Test
    void excluir_QuandoIdValido_DeveChamarDelete() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        produtoService.deletar(1L);

        verify(produtoRepository).delete(produto);
    }

}

