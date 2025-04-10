package com.projetointegrador.estoque.service;

import com.projetointegrador.estoque.dto.VendaItemDTO;
import com.projetointegrador.estoque.model.VendaItem;
import com.projetointegrador.estoque.repository.VendaItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendaItemService {

    private final VendaItemRepository vendaItemRepository;

    public VendaItemService(VendaItemRepository vendaItemRepository) {
        this.vendaItemRepository = vendaItemRepository;
    }

    public List<VendaItemDTO> listarTodos() {
        return vendaItemRepository.findAll()
                .stream()
                .map(vendaItem -> new VendaItemDTO(vendaItem.getId(), vendaItem.getVenda(), vendaItem.getProduto(), vendaItem.getQuantidade(), vendaItem.getValor(), vendaItem.calcularSubtotal()))
                .toList();
    }

    public VendaItemDTO buscarPorId(Long id) {
        VendaItem vendaItem = vendaItemRepository.findById(id)
                .orElseThrow();
        return new VendaItemDTO(vendaItem.getId(), vendaItem.getVenda(), vendaItem.getProduto(), vendaItem.getQuantidade(), vendaItem.getValor(), vendaItem.calcularSubtotal());
    }

    public VendaItemDTO cadastrar(VendaItemDTO dto) {
        vendaItemRepository.findById(dto.id());
        VendaItem novoVendaItem = vendaItemRepository.save(new VendaItem(dto));
        return new VendaItemDTO(novoVendaItem.getId(), novoVendaItem.getVenda(), novoVendaItem.getProduto(), novoVendaItem.getQuantidade(), novoVendaItem.getValor(), novoVendaItem.calcularSubtotal());
    }

    public VendaItemDTO atualizar(Long id, VendaItemDTO dto) {
        VendaItem vendaItem = vendaItemRepository.findById(id)
                .orElseThrow();

        vendaItemRepository.save(vendaItem);

        return new VendaItemDTO(vendaItem.getId(), vendaItem.getVenda(), vendaItem.getProduto(), vendaItem.getQuantidade(), vendaItem.getValor(), vendaItem.calcularSubtotal());
    }

    public VendaItemDTO atualizarAtributo(Long id, VendaItemDTO dto) {
        return atualizar(id, dto);
    }

    public void deletar(Long id) {
        vendaItemRepository.existsById(id);
        vendaItemRepository.deleteById(id);
    }
}
