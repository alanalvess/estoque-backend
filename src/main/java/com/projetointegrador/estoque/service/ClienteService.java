package com.projetointegrador.estoque.service;

import com.projetointegrador.estoque.dto.ClienteDTO;
import com.projetointegrador.estoque.exeption.ClienteDuplicadoException;
import com.projetointegrador.estoque.exeption.FornecedorNaoEncontradoException;
import com.projetointegrador.estoque.model.Cliente;
import com.projetointegrador.estoque.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<ClienteDTO> listarTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(cliente -> new ClienteDTO(cliente.getId(), cliente.getNome(), cliente.getCpf(), cliente.getEmail(), cliente.getTelefone()))
                .toList();
    }

    public ClienteDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new FornecedorNaoEncontradoException(id));
        return new ClienteDTO(cliente.getId(), cliente.getNome(), cliente.getCpf(), cliente.getEmail(), cliente.getTelefone());
    }

    public List<ClienteDTO> buscarPorCPF(String cpf) {
        return clienteRepository.findByCpf(cpf)
                .stream()
                .map(cliente -> new ClienteDTO(cliente.getId(), cliente.getNome(), cliente.getCpf(), cliente.getEmail(), cliente.getTelefone()))
                .toList();
    }

    public ClienteDTO cadastrar(ClienteDTO dto) {
        if (clienteRepository.findByCpf(dto.cpf()).isPresent()) {
            throw new ClienteDuplicadoException(dto.cpf());
        }
        Cliente novoCliente = clienteRepository.save(new Cliente(dto));
        return new ClienteDTO(
                novoCliente.getId(),
                novoCliente.getNome(),
                novoCliente.getCpf(),
                novoCliente.getEmail(),
                novoCliente.getTelefone()
        );
    }

    public ClienteDTO atualizar(Long id, ClienteDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new FornecedorNaoEncontradoException(id));

        clienteRepository.save(cliente);

        return new ClienteDTO(cliente.getId(), cliente.getNome(), cliente.getCpf(), cliente.getEmail(), cliente.getTelefone());
    }

    public ClienteDTO atualizarAtributo(Long id, ClienteDTO dto) {
        return atualizar(id, dto);
    }

    public void deletar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new FornecedorNaoEncontradoException(id);
        }
        clienteRepository.deleteById(id);
    }
}
