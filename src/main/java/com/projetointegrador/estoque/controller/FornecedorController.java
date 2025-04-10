package com.projetointegrador.estoque.controller;

import com.projetointegrador.estoque.dto.FornecedorDTO;
import com.projetointegrador.estoque.service.FornecedorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fornecedores")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FornecedorController {

    private final FornecedorService fornecedorService;

    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<FornecedorDTO>> listarTodos() {
        return ResponseEntity.status(HttpStatus.OK).body(fornecedorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FornecedorDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(fornecedorService.buscarPorId(id));
    }

    @GetMapping("/buscar/{cnpj}")
    public ResponseEntity<FornecedorDTO> buscarPorCNPJ(@PathVariable String cnpj) {
        return ResponseEntity.status(HttpStatus.OK).body(fornecedorService.buscarPorCNPJ(cnpj));
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<FornecedorDTO> cadastrar(@RequestBody @Valid FornecedorDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fornecedorService.cadastrar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FornecedorDTO> atualizar(@PathVariable Long id, @RequestBody @Valid FornecedorDTO dto) {
        return fornecedorService.atualizar(id, dto)
                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FornecedorDTO> atualizarAtributo(@PathVariable Long id, @RequestBody FornecedorDTO dto) {
        return fornecedorService.atualizarAtributo(id, dto)
                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        fornecedorService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
