package com.projetointegrador.estoque.service;

import com.projetointegrador.estoque.dto.FornecedorDTO;
import com.projetointegrador.estoque.exeption.FornecedorDuplicadoException;
import com.projetointegrador.estoque.model.Fornecedor;
import com.projetointegrador.estoque.repository.FornecedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    public FornecedorService(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    public List<FornecedorDTO> listarTodos() {
        return fornecedorRepository.findAll()
                .stream()
                .map(this::mapearParaDTO)
                .toList();
    }

    public FornecedorDTO buscarPorId(Long id) {
        Fornecedor fornecedor = buscarFornecedor(id);
        return mapearParaDTO(fornecedor);
    }

    public List<FornecedorDTO> buscarPorNome(String nome) {
        List<Fornecedor> fornecedores = fornecedorRepository.findAllByNomeContainingIgnoreCase(nome);

        return fornecedores.stream()
                .map(this::mapearParaDTO)
                .toList();
    }

    public List<FornecedorDTO> buscarPorCNPJ(String cnpj) {
        List<Fornecedor> fornecedores = fornecedorRepository.findAllByCnpjContainingIgnoreCase(cnpj);
        return fornecedores.stream()
                .map(this::mapearParaDTO)
                .toList();
    }

    public FornecedorDTO cadastrar(FornecedorDTO dto) {

        if (fornecedorRepository.findByNomeIgnoreCase(dto.nome()).isPresent()) {
            throw new FornecedorDuplicadoException("Nome", dto.nome());
        }

        if (!fornecedorRepository.findAllByCnpjContainingIgnoreCase(dto.cnpj()).isEmpty()) {
            throw new FornecedorDuplicadoException("CNPJ", dto.cnpj());
        }

        Fornecedor fornecedor = new Fornecedor(dto);
        return mapearParaDTO(fornecedorRepository.save(fornecedor));
    }

    public Optional<FornecedorDTO> atualizar(Long id, FornecedorDTO dto) {
        Fornecedor fornecedor = buscarFornecedor(id);

        if (dto.nome() == null || dto.cnpj() == null) {
            throw new IllegalArgumentException("Favor informar todos os itens obrigatorios");
        }

        fornecedor.setNome(dto.nome());
        fornecedor.setCnpj(dto.cnpj());
        fornecedor.setEmail(dto.email());
        fornecedor.setTelefone(dto.telefone());
        fornecedor.setEndereco(dto.endereco());

        return Optional.of(mapearParaDTO(fornecedorRepository.save(fornecedor)));
    }

    public Optional<FornecedorDTO> atualizarAtributo(Long id, FornecedorDTO dto) {
        Fornecedor fornecedor = buscarFornecedor(id);
        atualizarDadosFornecedor(fornecedor, dto);

        return Optional.of(mapearParaDTO(fornecedorRepository.save(fornecedor)));
    }

    public void deletar(Long id) {
        Fornecedor fornecedor = buscarFornecedor(id);
        fornecedorRepository.delete(fornecedor);
    }


    private FornecedorDTO mapearParaDTO(Fornecedor fornecedor) {
        return new FornecedorDTO(
                fornecedor.getId(),
                fornecedor.getNome(),
                fornecedor.getCnpj(),
                fornecedor.getEmail(),
                fornecedor.getTelefone(),
                fornecedor.getEndereco()
        );
    }

    private Fornecedor buscarFornecedor(Long id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fornecedor com ID " + id + " não localizado"));
    }

    private void atualizarDadosFornecedor(Fornecedor fornecedor, FornecedorDTO dto) {
        if (dto.nome() != null) fornecedor.setNome(dto.nome());
        if (dto.cnpj() != null) fornecedor.setCnpj(dto.cnpj());
        if (dto.email() != null) fornecedor.setEmail(dto.email());
        if (dto.telefone() != null) fornecedor.setTelefone(dto.telefone());
        if (dto.endereco() != null) fornecedor.setEndereco(dto.endereco());
    }
}
