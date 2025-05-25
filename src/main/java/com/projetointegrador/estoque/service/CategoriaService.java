package com.projetointegrador.estoque.service;

import com.projetointegrador.estoque.dto.CategoriaDTO;
import com.projetointegrador.estoque.model.Categoria;
import com.projetointegrador.estoque.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<CategoriaDTO> listarTodas() {
        return categoriaRepository.findAll()
                .stream()
                .map(this::mapearParaDTO)
                .toList();
    }

    public CategoriaDTO buscarPorId(Long id) {
        Categoria categoria = buscarCategoria(id);
        return mapearParaDTO(categoria);
    }

    public List<CategoriaDTO> buscarPorNome(String nome) {
        List<Categoria> categorias = categoriaRepository.findAllByNomeContainingIgnoreCase(nome);

        return categorias.stream()
                .map(this::mapearParaDTO)
                .toList();
    }

    public CategoriaDTO cadastrar(CategoriaDTO dto) {
        if (categoriaRepository.findByNomeIgnoreCase(dto.nome()).isPresent()) {
            throw new IllegalArgumentException("Já existe uma categoria com o nome " + dto.nome());
        }
        Categoria categoria = new Categoria(dto);
        return mapearParaDTO(categoriaRepository.save(categoria));
    }

    public Optional<CategoriaDTO> atualizar(Long id, CategoriaDTO dto) {
        Categoria categoria = buscarCategoria(id);

        if (dto.nome() == null) {
            throw new IllegalArgumentException("Favor informar todos os itens obrigatorios");
        }

        categoria.setNome(dto.nome());
        return Optional.of(mapearParaDTO(categoriaRepository.save(categoria)));
    }

    public Optional<CategoriaDTO> atualizarAtributo(Long id, CategoriaDTO dto) {
        Categoria categoria = buscarCategoria(id);
        atualizarDadosCategoria(categoria, dto);

        return Optional.of(mapearParaDTO(categoriaRepository.save(categoria)));
    }

    public void deletar(Long id) {
        Categoria categoria = buscarCategoria(id);
        categoriaRepository.delete(categoria);
    }

    private CategoriaDTO mapearParaDTO(Categoria categoria) {
        return new CategoriaDTO(
                categoria.getId(),
                categoria.getNome()
        );
    }

    private Categoria buscarCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria com ID " + id + " não localizado"));
    }

    private void atualizarDadosCategoria(Categoria categoria, CategoriaDTO dto) {
        if (dto.nome() != null) categoria.setNome(dto.nome());
    }
}
