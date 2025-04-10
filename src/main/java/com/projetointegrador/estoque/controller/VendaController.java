package com.projetointegrador.estoque.controller;

import com.projetointegrador.estoque.model.Venda;
import com.projetointegrador.estoque.repository.CategoriaRepository;
import com.projetointegrador.estoque.repository.VendaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vendas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VendaController {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public ResponseEntity<List<Venda>> getAll() {
        return ResponseEntity.ok(vendaRepository.findAll());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Venda>> getAllVendas() {
        return ResponseEntity.ok(vendaRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venda> getById(@PathVariable Long id) {
        return vendaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Venda> post(@Valid @RequestBody Venda venda) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vendaRepository.save(venda));

    }

    @PutMapping
    public ResponseEntity<Venda> put(@Valid @RequestBody Venda venda) {
        return ResponseEntity.status(HttpStatus.OK).body(vendaRepository.save(venda));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Optional<Venda> venda = vendaRepository.findById(id);

        if (venda.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        vendaRepository.deleteById(id);
    }

}
