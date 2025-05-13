package com.projetointegrador.estoque.service;

import com.projetointegrador.estoque.dto.MarcaDTO;
import com.projetointegrador.estoque.model.Marca;
import com.projetointegrador.estoque.repository.MarcaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MarcaService {

    private final MarcaRepository marcaRepository;

    public MarcaService(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    public List<MarcaDTO> listarTodas() {
        return marcaRepository.findAll()
                .stream()
                .map(this::mapearParaDTO)
                .toList();
    }

    public MarcaDTO buscarPorId(Long id) {
        Marca marca = buscarMarca(id);
        return mapearParaDTO(marca);
    }

    public List<MarcaDTO> buscarPorNome(String nome) {
        List<Marca> marcas = marcaRepository.findAllByNomeContainingIgnoreCase(nome);

        return marcas.stream()
                .map(this::mapearParaDTO)
                .toList();
    }

    public MarcaDTO cadastrar(MarcaDTO dto) {
        if (marcaRepository.findByNomeIgnoreCase(dto.nome()).isPresent()) {
            throw new IllegalArgumentException("Já existe uma marca com o nome " + dto.nome());
        }
        Marca marca = new Marca(dto);
        return mapearParaDTO(marcaRepository.save(marca));
    }

    public Optional<MarcaDTO> atualizar(Long id, MarcaDTO dto) {
        Marca marca = buscarMarca(id);

        if (dto.nome() == null) {
            throw new IllegalArgumentException("Favor informar todos os itens obrigatorios");
        }

        marca.setNome(dto.nome());
        return Optional.of(mapearParaDTO(marcaRepository.save(marca)));
    }

    public Optional<MarcaDTO> atualizarAtributo(Long id, MarcaDTO dto) {
        Marca marca = buscarMarca(id);
        atualizarDadosMarca(marca, dto);

        return Optional.of(mapearParaDTO(marcaRepository.save(marca)));
    }

    public void deletar(Long id) {
        Marca marca = buscarMarca(id);
        marcaRepository.delete(marca);
    }

    private MarcaDTO mapearParaDTO(Marca marca) {
        return new MarcaDTO(
                marca.getId(),
                marca.getNome()
        );
    }

    private Marca buscarMarca(Long id) {
        return marcaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Marca com ID " + id + " não localizado"));
    }

    private void atualizarDadosMarca(Marca marca, MarcaDTO dto) {
        if (dto.nome() != null) marca.setNome(dto.nome());

    }
}
