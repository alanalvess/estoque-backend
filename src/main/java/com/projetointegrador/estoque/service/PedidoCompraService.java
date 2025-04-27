package com.projetointegrador.estoque.service;

import com.projetointegrador.estoque.dto.PedidoCompraDTO;
import com.projetointegrador.estoque.model.Fornecedor;
import com.projetointegrador.estoque.model.PedidoCompra;
import com.projetointegrador.estoque.model.PedidoItem;
import com.projetointegrador.estoque.repository.FornecedorRepository;
import com.projetointegrador.estoque.repository.PedidoCompraRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoCompraService {

    private final PedidoCompraRepository pedidoCompraRepository;
    private final FornecedorRepository fornecedorRepository;

    public PedidoCompraService(PedidoCompraRepository pedidoCompraRepository, FornecedorRepository fornecedorRepository) {
        this.pedidoCompraRepository = pedidoCompraRepository;
        this.fornecedorRepository = fornecedorRepository;
    }

    public List<PedidoCompraDTO> listarTodos() {
        return pedidoCompraRepository.findAll()
                .stream()
                .map(pedidoCompra -> new PedidoCompraDTO(pedidoCompra.getId(), pedidoCompra.getDataPedido(), pedidoCompra.getFornecedor(), pedidoCompra.getStatus(), pedidoCompra.getTotal()))
                .toList();
    }

    public PedidoCompraDTO buscarPorId(Long id) {
        PedidoCompra pedidoCompra = pedidoCompraRepository.findById(id)
                .orElseThrow();

        return new PedidoCompraDTO(pedidoCompra.getId(), pedidoCompra.getDataPedido(), pedidoCompra.getFornecedor(), pedidoCompra.getStatus(), pedidoCompra.getTotal());
    }

    public PedidoCompraDTO cadastrar(PedidoCompraDTO dto) {
        Fornecedor fornecedor = fornecedorRepository.findById(dto.fornecedor().getId())
                .orElseThrow(() -> new IllegalArgumentException(dto.fornecedor().getCnpj()));

        PedidoCompra novoPedido = new PedidoCompra(dto);
        novoPedido.setFornecedor(fornecedor);

        PedidoCompra pedidoSalvo = pedidoCompraRepository.save(novoPedido);
        return new PedidoCompraDTO(pedidoSalvo.getId(), pedidoSalvo.getDataPedido(), pedidoSalvo.getFornecedor(), pedidoSalvo.getStatus(), pedidoSalvo.getTotal());
    }

    public PedidoCompraDTO atualizar(Long id, PedidoCompraDTO dto) {
        PedidoCompra pedidoCompra = pedidoCompraRepository.findById(id)
                .orElseThrow();

        Fornecedor fornecedor = fornecedorRepository.findById(dto.fornecedor().getId())
                .orElseThrow(() -> new IllegalArgumentException(dto.fornecedor().getCnpj()));

        pedidoCompra.setFornecedor(fornecedor);
        pedidoCompraRepository.save(pedidoCompra);

        return new PedidoCompraDTO(pedidoCompra.getId(), pedidoCompra.getDataPedido(), pedidoCompra.getFornecedor(), pedidoCompra.getStatus(), pedidoCompra.getTotal());
    }

    public PedidoCompraDTO atualizarAtributo(Long id, PedidoCompraDTO dto) {
        return atualizar(id, dto);
    }

    public void deletar(Long id) {
        if (!pedidoCompraRepository.existsById(id)) ;
        pedidoCompraRepository.deleteById(id);
    }
}
