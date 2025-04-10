package com.projetointegrador.estoque.controller;

import com.projetointegrador.estoque.dto.UsuarioDTO;
import com.projetointegrador.estoque.dto.UsuarioLoginDTO;
import com.projetointegrador.estoque.dto.UsuarioRequestDTO;
import com.projetointegrador.estoque.model.Usuario;
import com.projetointegrador.estoque.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UsuarioRequestDTO>> listarTodos() {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioRequestDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.buscarPorId(id));
    }

    @GetMapping("/buscar/{email}")
    public ResponseEntity<UsuarioRequestDTO> buscarPorEmail(@PathVariable String email) {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.buscarPorEmail(email));
    }

    @PostMapping("/logar")
    public ResponseEntity<UsuarioDTO> autenticarUsuario(@RequestBody UsuarioLoginDTO usuarioLoginDTO) {
        return usuarioService.autenticarUsuario(usuarioLoginDTO)
                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioDTO> cadastrarUsuario(@RequestBody @Valid Usuario usuario, Authentication authentication) {
        UsuarioDTO resposta = usuarioService.cadastrar(usuario, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<Usuario> atualizar(@RequestBody @Valid Usuario usuario) {
        return usuarioService.atualizar(usuario)
                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/atualizar")
    public ResponseEntity<Optional<Usuario>> atualizarAtributo(@RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.atualizarAtributo(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername(); // pega o email do usuário logado
        usuarioService.deletar(id, email);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // HTTP 204
    }

}