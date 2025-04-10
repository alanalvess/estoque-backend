package com.projetointegrador.estoque.controller;

import com.projetointegrador.estoque.dto.PedidoItemDTO;
import com.projetointegrador.estoque.service.ClienteService;
import com.projetointegrador.estoque.service.PedidoItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidoItemes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PedidoItemController {

    private final PedidoItemService pedidoItemService;

    public PedidoItemController(PedidoItemService pedidoItemService, ClienteService clienteService) {
        this.pedidoItemService = pedidoItemService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<PedidoItemDTO>> listarTodos() {
        return ResponseEntity.ok(pedidoItemService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoItemDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoItemService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<PedidoItemDTO> cadastrar(@RequestBody @Valid PedidoItemDTO dto) {
        return ResponseEntity.ok(pedidoItemService.cadastrar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoItemDTO> atualizar(@PathVariable Long id, @RequestBody @Valid PedidoItemDTO dto) {
        return ResponseEntity.ok(pedidoItemService.atualizar(id, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PedidoItemDTO> atualizarAtributo(@PathVariable Long id, @RequestBody PedidoItemDTO dto) {
        return ResponseEntity.ok(pedidoItemService.atualizarAtributo(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pedidoItemService.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
