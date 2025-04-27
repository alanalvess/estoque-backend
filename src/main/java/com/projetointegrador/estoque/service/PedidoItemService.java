package com.projetointegrador.estoque.service;

import com.projetointegrador.estoque.dto.PedidoItemDTO;
import com.projetointegrador.estoque.model.PedidoItem;
import com.projetointegrador.estoque.repository.PedidoItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoItemService {

    private final PedidoItemRepository pedidoItemRepository;

    public PedidoItemService(PedidoItemRepository pedidoItemRepository) {
        this.pedidoItemRepository = pedidoItemRepository;
    }

    public List<PedidoItemDTO> listarTodos() {
        return pedidoItemRepository.findAll()
                .stream()
                .map(pedidoItem -> new PedidoItemDTO(pedidoItem.getId(), pedidoItem.getProduto(), pedidoItem.getQuantidade(), pedidoItem.getValor()))
                .toList();
    }

    public PedidoItemDTO buscarPorId(Long id) {
        PedidoItem pedidoItem = pedidoItemRepository.findById(id)
                .orElseThrow();
        return new PedidoItemDTO(pedidoItem.getId(), pedidoItem.getProduto(), pedidoItem.getQuantidade(), pedidoItem.getValor());
    }

    public PedidoItemDTO cadastrar(PedidoItemDTO dto) {
        pedidoItemRepository.findById(dto.id());
        PedidoItem novoPedidoItem = pedidoItemRepository.save(new PedidoItem(dto));
        return new PedidoItemDTO(
                novoPedidoItem.getId(),
                novoPedidoItem.getProduto(),
                novoPedidoItem.getQuantidade(),
                novoPedidoItem.getValor()
        );
    }

    public PedidoItemDTO atualizar(Long id, PedidoItemDTO dto) {
        PedidoItem pedidoItem = pedidoItemRepository.findById(id)
                .orElseThrow();

        pedidoItemRepository.save(pedidoItem);

        return new PedidoItemDTO(pedidoItem.getId(), pedidoItem.getProduto(), pedidoItem.getQuantidade(), pedidoItem.getValor());
    }

    public PedidoItemDTO atualizarAtributo(Long id, PedidoItemDTO dto) {
        return atualizar(id, dto);
    }

    public void deletar(Long id) {
        pedidoItemRepository.existsById(id);
        pedidoItemRepository.deleteById(id);
    }
}
