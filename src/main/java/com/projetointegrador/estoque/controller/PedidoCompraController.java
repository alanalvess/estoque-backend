package com.projetointegrador.estoque.controller;

import com.projetointegrador.estoque.dto.PedidoCompraDTO;
import com.projetointegrador.estoque.service.ClienteService;
import com.projetointegrador.estoque.service.PedidoCompraService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidoCompraes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PedidoCompraController {

    private final PedidoCompraService pedidoCompraService;

    public PedidoCompraController(PedidoCompraService pedidoCompraService, ClienteService clienteService) {
        this.pedidoCompraService = pedidoCompraService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<PedidoCompraDTO>> listarTodos() {
        return ResponseEntity.ok(pedidoCompraService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoCompraDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoCompraService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<PedidoCompraDTO> cadastrar(@RequestBody @Valid PedidoCompraDTO dto) {
        return ResponseEntity.ok(pedidoCompraService.cadastrar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoCompraDTO> atualizar(@PathVariable Long id, @RequestBody @Valid PedidoCompraDTO dto) {
        return ResponseEntity.ok(pedidoCompraService.atualizar(id, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PedidoCompraDTO> atualizarAtributo(@PathVariable Long id, @RequestBody PedidoCompraDTO dto) {
        return ResponseEntity.ok(pedidoCompraService.atualizarAtributo(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pedidoCompraService.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
