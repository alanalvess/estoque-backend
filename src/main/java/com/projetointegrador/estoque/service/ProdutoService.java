package com.projetointegrador.estoque.service;

import com.projetointegrador.estoque.dto.ProdutoDTO;
import com.projetointegrador.estoque.exeption.CategoriaNaoEncontradaException;
import com.projetointegrador.estoque.exeption.FornecedorNaoEncontradoException;
import com.projetointegrador.estoque.exeption.ProdutoDuplicadoException;
import com.projetointegrador.estoque.exeption.ProdutoNaoEncontradoException;
import com.projetointegrador.estoque.model.Categoria;
import com.projetointegrador.estoque.model.Fornecedor;
import com.projetointegrador.estoque.model.Produto;
import com.projetointegrador.estoque.repository.CategoriaRepository;
import com.projetointegrador.estoque.repository.FornecedorRepository;
import com.projetointegrador.estoque.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final FornecedorRepository fornecedorRepository;

    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository, FornecedorRepository fornecedorRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.fornecedorRepository = fornecedorRepository;
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

    public ProdutoDTO buscarPorNome(String nome) {
        Produto produto = produtoRepository.findByNomeIgnoreCase(nome)
                .orElseThrow(() -> new IllegalArgumentException("Produto com nome " + nome + " não localizado"));
        return mapearParaDTO(produto);
    }

    public ProdutoDTO cadastrar(ProdutoDTO dto) {

        if (produtoRepository.findByNomeIgnoreCase(dto.nome()).isPresent()) {
            throw new ProdutoDuplicadoException(dto.nome());
        }

        Fornecedor fornecedor = fornecedorRepository.findById(dto.fornecedor().getId())
                .orElseThrow(() -> new ProdutoNaoEncontradoException(dto.fornecedor().getId()));

        Categoria categoria = categoriaRepository.findById(dto.categoria().getId())
                .orElseThrow(() -> new CategoriaNaoEncontradaException(dto.categoria().getId()));

        if (dto.disponivel() && (dto.quantidade() == null || dto.quantidade() <= 0)) {
            throw new IllegalArgumentException("Produto não pode estar disponível com quantidade zero.");
        }

        Produto produto = new Produto(dto);
        produto.setFornecedor(fornecedor);
        produto.setCategoria(categoria);

        Produto salvo = produtoRepository.save(produto);
        return mapearParaDTO(salvo);

    }

    public Optional<ProdutoDTO> atualizar(Long id, ProdutoDTO dto) {
        Produto produto = buscarProduto(id);

        Categoria categoria = categoriaRepository.findById(dto.categoria().getId())
                .orElseThrow(() -> new CategoriaNaoEncontradaException(dto.categoria().getId()));
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
        produto.setMarca(dto.marca());
        produto.setEstoqueMinimo(dto.estoqueMinimo());
        produto.setEstoqueMaximo(dto.estoqueMaximo());
        produto.setValidade(dto.validade());
        produto.setDataEntrada(dto.dataEntrada());
        produto.setDataSaida(dto.dataSaida());
        produto.setCategoria(categoria);
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
                produto.getMarca(),
                produto.getEstoqueMinimo(),
                produto.getEstoqueMaximo(),
                produto.getValidade(),
                produto.getDataEntrada(),
                produto.getDataSaida(),
                produto.getCategoria(),
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

        // Validação da disponibilidade
        if (dto.disponivel() != null) {
            Integer quantidadeAtualizada = dto.quantidade() != null ? dto.quantidade() : produto.getQuantidade();
            if (dto.disponivel() && (quantidadeAtualizada == null || quantidadeAtualizada <= 0)) {
                throw new IllegalArgumentException("Produto não pode estar disponível com quantidade zero.");
            }
            produto.setDisponivel(dto.disponivel());
        }

        if (dto.codigo() != null) produto.setCodigo(dto.codigo());
        if (dto.marca() != null) produto.setMarca(dto.marca());
        if (dto.estoqueMinimo() != null) produto.setEstoqueMinimo(dto.estoqueMinimo());
        if (dto.estoqueMaximo() != null) produto.setEstoqueMaximo(dto.estoqueMaximo());
        if (dto.validade() != null) produto.setValidade(dto.validade());
        if (dto.dataEntrada() != null) produto.setDataEntrada(dto.dataEntrada());
        if (dto.dataSaida() != null) produto.setDataSaida(dto.dataSaida());
        if (dto.unidadeMedida() != null) produto.setUnidadeMedida(dto.unidadeMedida());
        if (dto.categoria() != null && dto.categoria().getId() != null) {
            Categoria categoria = categoriaRepository.findById(dto.categoria().getId())
                    .orElseThrow(() -> new CategoriaNaoEncontradaException(dto.categoria().getId()));
            produto.setCategoria(categoria);
        }

        if (dto.fornecedor() != null && dto.fornecedor().getId() != null) {
            Fornecedor fornecedor = fornecedorRepository.findById(dto.fornecedor().getId())
                    .orElseThrow(() -> new FornecedorNaoEncontradoException(dto.fornecedor().getId()));
            produto.setFornecedor(fornecedor);
        }
    }



    private Fornecedor buscarFornecedor(Long id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fornecedor com ID " + id + " não localizado"));
    }

    private Categoria buscarCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria com ID " + id + " não localizada"));
    }
    public void adicionarEstoque(Long id, int quantidade) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));

        produtoRepository.save(produto);
    }

    public void removerEstoque(Long id, int quantidade) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));

        produtoRepository.save(produto);
    }
}
