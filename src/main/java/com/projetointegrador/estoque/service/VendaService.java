package com.projetointegrador.estoque.service;

import com.projetointegrador.estoque.dto.VendaDTO;
import com.projetointegrador.estoque.model.Venda;
import com.projetointegrador.estoque.repository.VendaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;

    public VendaService(VendaRepository vendaRepository) {
        this.vendaRepository = vendaRepository;
    }

    public List<VendaDTO> listarTodos() {
        return vendaRepository.findAll()
                .stream()
                .map(venda -> new VendaDTO(venda.getId(), venda.getDataVenda(), venda.getCliente(), venda.getTotal()))
                .toList();
    }

    public VendaDTO buscarPorId(Long id) {
        Venda venda = vendaRepository.findById(id)
                .orElseThrow();
        return new VendaDTO(venda.getId(), venda.getDataVenda(), venda.getCliente(), venda.getTotal());
    }

    public VendaDTO cadastrar(VendaDTO dto) {
        vendaRepository.findById(dto.id());
        Venda novoVenda = vendaRepository.save(new Venda(dto));
        return new VendaDTO(novoVenda.getId(), novoVenda.getDataVenda(), novoVenda.getCliente(), novoVenda.getTotal());
    }

    public VendaDTO atualizar(Long id, VendaDTO dto) {
        Venda venda = vendaRepository.findById(id)
                .orElseThrow();

        vendaRepository.save(venda);

        return new VendaDTO(venda.getId(), venda.getDataVenda(), venda.getCliente(), venda.getTotal());
    }

    public VendaDTO atualizarAtributo(Long id, VendaDTO dto) {
        return atualizar(id, dto);
    }

    public void deletar(Long id) {
        vendaRepository.existsById(id);
        vendaRepository.deleteById(id);
    }
}
