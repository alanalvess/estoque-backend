package com.projetointegrador.estoque.controller;

import com.projetointegrador.estoque.dto.ClienteDTO;
import com.projetointegrador.estoque.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ClienteDTO>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ClienteDTO>> buscarPorCPF(@RequestParam String cpf) {
        return ResponseEntity.ok(clienteService.buscarPorCPF(cpf));
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> cadastrar(@RequestBody @Valid ClienteDTO dto) {
        return ResponseEntity.ok(clienteService.cadastrar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ClienteDTO dto) {
        return ResponseEntity.ok(clienteService.atualizar(id, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClienteDTO> atualizarAtributo(@PathVariable Long id, @RequestBody ClienteDTO dto) {
        return ResponseEntity.ok(clienteService.atualizarAtributo(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
