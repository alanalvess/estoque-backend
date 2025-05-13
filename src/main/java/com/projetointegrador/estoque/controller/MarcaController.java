package com.projetointegrador.estoque.controller;

import com.projetointegrador.estoque.dto.MarcaDTO;
import com.projetointegrador.estoque.service.MarcaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/marcas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MarcaController {

    private final MarcaService marcaService;

    public MarcaController(MarcaService marcaService) {
        this.marcaService = marcaService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<MarcaDTO>> listarTodas() {
        return ResponseEntity.status(HttpStatus.OK).body(marcaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarcaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(marcaService.buscarPorId(id));
    }

    @GetMapping("/buscar/{nome}")
    public ResponseEntity<List<MarcaDTO>> buscarPorNome(@PathVariable String nome) {
        return ResponseEntity.status(HttpStatus.OK).body(marcaService.buscarPorNome(nome));
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<MarcaDTO> cadastrar(@RequestBody @Valid MarcaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(marcaService.cadastrar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarcaDTO> atualizar(@PathVariable Long id, @RequestBody @Valid MarcaDTO dto) {
        return marcaService.atualizar(id, dto)
                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MarcaDTO> atualizarAtributo(@PathVariable Long id, @RequestBody MarcaDTO dto) {
        return marcaService.atualizarAtributo(id, dto)
                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        marcaService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
