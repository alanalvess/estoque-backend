package com.projetointegrador.estoque.controller;

import com.projetointegrador.estoque.dto.CategoriaDTO;
import com.projetointegrador.estoque.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoriaDTO>> listarTodas() {
        return ResponseEntity.status(HttpStatus.OK).body(categoriaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(categoriaService.buscarPorId(id));
    }

    @GetMapping("/buscar/{nome}")
    public ResponseEntity<CategoriaDTO> buscarPorNome(@PathVariable String nome) {
        return ResponseEntity.status(HttpStatus.OK).body(categoriaService.buscarPorNome(nome));
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<CategoriaDTO> cadastrar(@RequestBody @Valid CategoriaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.cadastrar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> atualizar(@PathVariable Long id, @RequestBody @Valid CategoriaDTO dto) {
        return categoriaService.atualizar(id, dto)
                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoriaDTO> atualizarAtributo(@PathVariable Long id, @RequestBody CategoriaDTO dto) {
        return categoriaService.atualizarAtributo(id, dto)
                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
