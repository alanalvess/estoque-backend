package com.projetointegrador.estoque.service;

import com.projetointegrador.estoque.dto.FornecedorDTO;
import com.projetointegrador.estoque.dto.ProdutoDTO;
import com.projetointegrador.estoque.exeption.*;
import com.projetointegrador.estoque.model.Categoria;
import com.projetointegrador.estoque.model.Fornecedor;
import com.projetointegrador.estoque.model.Marca;
import com.projetointegrador.estoque.model.Produto;
import com.projetointegrador.estoque.repository.CategoriaRepository;
import com.projetointegrador.estoque.repository.FornecedorRepository;
import com.projetointegrador.estoque.repository.MarcaRepository;
import com.projetointegrador.estoque.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final FornecedorRepository fornecedorRepository;
    private final MarcaRepository marcaRepository;

    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository, MarcaRepository marcaRepository, FornecedorRepository fornecedorRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.marcaRepository = marcaRepository;
    }

    public List<ProdutoDTO> listarTodos() {
        return produtoRepository.findAll()
                .stream()
                .map(this::mapearParaDTO)
                .toList();
    }

    public ProdutoDTO buscarPorId(Long id) {
        Produto produto = buscarProduto(id);
        return mapearParaDTO(produto);
    }

    public List<ProdutoDTO> buscarPorNome(String nome) {
        List<Produto> produtos = produtoRepository.findAllByNomeContainingIgnoreCase(nome);

        return produtos.stream()
                .map(this::mapearParaDTO)
                .toList();
    }

    public List<ProdutoDTO> buscarPorCNPJ(String codigo) {
        List<Produto> produtos = produtoRepository.findAllByCodigoContainingIgnoreCase(codigo);
        return produtos.stream()
                .map(this::mapearParaDTO)
                .toList();
    }


    public ProdutoDTO cadastrar(ProdutoDTO dto) {

        if (produtoRepository.findByNomeIgnoreCase(dto.nome()).isPresent()) {
            throw new ProdutoDuplicadoException(dto.nome());
        }

        Categoria categoria = categoriaRepository.findById(dto.categoria().getId())
                .orElseThrow(() -> new CategoriaNaoEncontradaException(dto.categoria().getId()));

        Marca marca = marcaRepository.findById(dto.marca().getId())
                .orElseThrow(() -> new MarcaNaoEncontradaException(dto.marca().getId()));

        Fornecedor fornecedor = fornecedorRepository.findById(dto.fornecedor().getId())
                .orElseThrow(() -> new ProdutoNaoEncontradoException(dto.fornecedor().getId()));

        if (dto.disponivel() && (dto.quantidade() == null || dto.quantidade() <= 0)) {
            throw new IllegalArgumentException("Produto não pode estar disponível com quantidade zero.");
        }

        Produto produto = new Produto(dto);
        produto.setFornecedor(fornecedor);
        produto.setCategoria(categoria);
        produto.setMarca(marca);

        produto.setDataValidade(dto.dataValidade());
        produto.setDataEntrada(dto.dataEntrada());
        produto.setDataSaida(dto.dataSaida());

        if (dto.dataValidade() != null && dto.dataEntrada() != null &&
                dto.dataValidade().isBefore(dto.dataEntrada())) {
            throw new DatasInvalidasException("A data de validade não pode ser anterior à data de entrada.");
        }

        if (dto.dataSaida() != null && dto.dataEntrada() != null &&
                dto.dataSaida().isBefore(dto.dataEntrada())) {
            throw new DatasInvalidasException("A data de saída não pode ser anterior à data de entrada.");
        }

        if (dto.estoqueMinimo() != null && dto.estoqueMaximo() != null &&
                dto.estoqueMaximo() < dto.estoqueMinimo()) {
            throw new IllegalArgumentException("O estoque máximo não pode ser menor que o estoque mínimo.");
        }

        if (dto.valor() != null && dto.valor().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor do produto não pode ser negativo.");
        }

        if (dto.quantidade() != null && dto.quantidade() < 0) {
            throw new IllegalArgumentException("A quantidade do produto não pode ser negativa.");
        }




        Produto salvo = produtoRepository.save(produto);

        return mapearParaDTO(salvo);
    }

    public Optional<ProdutoDTO> atualizar(Long id, ProdutoDTO dto) {
        Produto produto = buscarProduto(id);

        Categoria categoria = categoriaRepository.findById(dto.categoria().getId())
                .orElseThrow(() -> new CategoriaNaoEncontradaException(dto.categoria().getId()));

        Marca marca = marcaRepository.findById(dto.marca().getId())
                .orElseThrow(() -> new MarcaNaoEncontradaException(dto.marca().getId()));

        Fornecedor fornecedor = fornecedorRepository.findById(dto.fornecedor().getId())
                .orElseThrow(() -> new FornecedorNaoEncontradoException(dto.fornecedor().getId()));

        if (dto.disponivel() && (dto.quantidade() == null || dto.quantidade() <= 0)) {
            throw new IllegalArgumentException("Produto não pode estar disponível com quantidade zero.");
        }

        produto.setNome(dto.nome());
        produto.setDescricao(dto.descricao());
        produto.setValor(dto.valor());
        produto.setQuantidade(dto.quantidade());
        produto.setDisponivel(dto.disponivel());
        produto.setUnidadeMedida(dto.unidadeMedida());
        produto.setCodigo(dto.codigo());
        produto.setEstoqueMinimo(dto.estoqueMinimo());
        produto.setEstoqueMaximo(dto.estoqueMaximo());
        produto.setDataValidade(dto.dataValidade());
        produto.setDataEntrada(dto.dataEntrada());
        produto.setDataSaida(dto.dataSaida());

        if (dto.valor() != null && dto.valor().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor do produto não pode ser negativo.");
        }

        if (dto.quantidade() != null && dto.quantidade() < 0) {
            throw new IllegalArgumentException("A quantidade do produto não pode ser negativa.");
        }


        if (dto.dataValidade() != null && dto.dataEntrada() != null &&
                dto.dataValidade().isBefore(dto.dataEntrada())) {
            throw new DatasInvalidasException("A data de validade não pode ser anterior à data de entrada.");
        }

        if (dto.dataSaida() != null && dto.dataEntrada() != null &&
                dto.dataSaida().isBefore(dto.dataEntrada())) {
            throw new DatasInvalidasException("A data de saída não pode ser anterior à data de entrada.");
        }
        if (dto.estoqueMinimo() != null && dto.estoqueMaximo() != null &&
                dto.estoqueMaximo() < dto.estoqueMinimo()) {
            throw new IllegalArgumentException("O estoque máximo não pode ser menor que o estoque mínimo.");
        }



        produto.setCategoria(categoria);
        produto.setMarca(marca);
        produto.setFornecedor(fornecedor);



        return Optional.of(mapearParaDTO(produtoRepository.save(produto)));
    }

    public Optional<ProdutoDTO> atualizarAtributo(Long id, ProdutoDTO dto) {
        Produto produto = buscarProduto(id);
        atualizarDadosProduto(produto, dto);

        return Optional.of(mapearParaDTO(produtoRepository.save(produto)));
    }

    public void deletar(Long id) {
        Produto produto = buscarProduto(id);
        produtoRepository.delete(produto);
    }


    private ProdutoDTO mapearParaDTO(Produto produto) {
        return new ProdutoDTO(
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

    private Produto buscarProduto(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto com ID " + id + " não localizado"));
    }

    private void atualizarDadosProduto(Produto produto, ProdutoDTO dto) {
        if (dto.nome() != null) produto.setNome(dto.nome());
        if (dto.descricao() != null) produto.setDescricao(dto.descricao());
        if (dto.valor() != null) produto.setValor(dto.valor());
        if (dto.quantidade() != null) produto.setQuantidade(dto.quantidade());

        if (dto.disponivel() != null) {
            Integer quantidadeAtualizada = dto.quantidade() != null ? dto.quantidade() : produto.getQuantidade();
            if (dto.disponivel() && (quantidadeAtualizada == null || quantidadeAtualizada <= 0)) {
                throw new IllegalArgumentException("Produto não pode estar disponível com quantidade zero.");
            }
            produto.setDisponivel(dto.disponivel());
        }

        if (dto.codigo() != null) produto.setCodigo(dto.codigo());
        if (dto.estoqueMinimo() != null) produto.setEstoqueMinimo(dto.estoqueMinimo());
        if (dto.estoqueMaximo() != null) produto.setEstoqueMaximo(dto.estoqueMaximo());
        if (dto.dataValidade() != null)
            produto.setDataValidade(dto.dataValidade());
        if (dto.dataEntrada() != null)
            produto.setDataEntrada(dto.dataEntrada());
        if (dto.dataSaida() != null)
            produto.setDataSaida(dto.dataSaida());
        if (dto.unidadeMedida() != null) produto.setUnidadeMedida(dto.unidadeMedida());

        LocalDate entrada = dto.dataEntrada() != null ? dto.dataEntrada() : produto.getDataEntrada();
        LocalDate validade = dto.dataValidade() != null ? dto.dataValidade() : produto.getDataValidade();
        LocalDate saida    = dto.dataSaida() != null ? dto.dataSaida() : produto.getDataSaida();

        if (validade != null && entrada != null && validade.isBefore(entrada)) {
            throw new DatasInvalidasException("A data de validade não pode ser anterior à data de entrada.");
        }

        if (saida != null && entrada != null && saida.isBefore(entrada)) {
            throw new DatasInvalidasException("A data de saída não pode ser anterior à data de entrada.");
        }

        if (dto.estoqueMinimo() != null && dto.estoqueMaximo() != null &&
                dto.estoqueMaximo() < dto.estoqueMinimo()) {
            throw new IllegalArgumentException("O estoque máximo não pode ser menor que o estoque mínimo.");
        }

        if (dto.valor() != null && dto.valor().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor do produto não pode ser negativo.");
        }

        if (dto.quantidade() != null && dto.quantidade() < 0) {
            throw new IllegalArgumentException("A quantidade do produto não pode ser negativa.");
        }



        if (dto.categoria() != null && dto.categoria().getId() != null) {
            Categoria categoria = categoriaRepository.findById(dto.categoria().getId())
                    .orElseThrow(() -> new CategoriaNaoEncontradaException(dto.categoria().getId()));
            produto.setCategoria(categoria);
        }
        if (dto.marca() != null && dto.marca().getId() != null) {
            Marca marca = marcaRepository.findById(dto.marca().getId())
                    .orElseThrow(() -> new MarcaNaoEncontradaException(dto.marca().getId()));
            produto.setMarca(marca);        }

        if (dto.fornecedor() != null && dto.fornecedor().getId() != null) {
            Fornecedor fornecedor = fornecedorRepository.findById(dto.fornecedor().getId())
                    .orElseThrow(() -> new FornecedorNaoEncontradoException(dto.fornecedor().getId()));
            produto.setFornecedor(fornecedor);
        }
    }
}
