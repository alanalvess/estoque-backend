package com.projetointegrador.estoque.service;

import com.projetointegrador.estoque.dto.UsuarioDTO;
import com.projetointegrador.estoque.dto.UsuarioLoginDTO;
import com.projetointegrador.estoque.dto.UsuarioRequestDTO;
import com.projetointegrador.estoque.enums.Role;
import com.projetointegrador.estoque.exeption.AcessoNegadoException;
import com.projetointegrador.estoque.exeption.UsuarioNaoEncontradoException;
import com.projetointegrador.estoque.model.Usuario;
import com.projetointegrador.estoque.repository.UsuarioRepository;
import com.projetointegrador.estoque.security.JwtService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String criptografarSenha(String senha) {
        return passwordEncoder.encode(senha);
    }

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.name}")
    private String adminName;

    @PostConstruct
    public void criarAdminPadrao() {
        if (usuarioRepository.findByEmail(adminEmail).isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNome(adminName);
            admin.setEmail(adminEmail);
            admin.setSenha(passwordEncoder.encode(adminPassword));
            admin.getRoles().add(Role.ADMIN); // Define como ADMIN
            usuarioRepository.save(admin);
            System.out.println("✅ Usuário ADMIN criado com sucesso!");
        }
    }

    public List<UsuarioRequestDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::mapearParaDTO)
                .toList();
    }

    public UsuarioRequestDTO buscarPorId(Long id) {
        Usuario usuario = buscarUsuario(id);
        return mapearParaDTO(usuario);
    }

    public UsuarioRequestDTO buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário com e-mail " + email + "não localizado"));
        return mapearParaDTO(usuario);
    }

    public UsuarioDTO cadastrar(Usuario usuario, Authentication authentication) {
        verificarSeEmailJaExiste(usuario.getEmail());

        if (usuarioDesejaSerAdmin(usuario)) {
            if (authentication == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "É necessário estar autenticado como ADMIN para criar outro ADMIN");
            }

            validarCriacaoDeAdmin(authentication);
        }

        atribuirRolePadraoSeNecessario(usuario);
        usuario.setSenha(criptografarSenha(usuario.getSenha()));

        Usuario salvo = usuarioRepository.save(usuario);

        return new UsuarioDTO(
                salvo.getId(),
                salvo.getNome(),
                salvo.getEmail(),
                null, // token pode ser preenchido depois no login
                salvo.getRoles()
        );
    }

    public Optional<Usuario> atualizar(Usuario usuario) {
        return usuarioRepository.findById(usuario.getId()).map(existingUser -> {

            // Verifica se o novo e-mail já está sendo usado por outro usuário
            usuarioRepository.findByEmail(usuario.getEmail())
                    .filter(user -> !user.getId().equals(usuario.getId()))
                    .ifPresent(user -> {
                        throw new IllegalArgumentException("E-mail cadastrado para outro usuário");
                    });

            // Atualiza os campos que mudaram
            existingUser.setNome(usuario.getNome()); // exemplo: se tiver campo nome

            if (!existingUser.getEmail().equals(usuario.getEmail())) {
                existingUser.setEmail(usuario.getEmail());
            }

            if (!usuario.getSenha().equals(existingUser.getSenha())) {
                existingUser.setSenha(passwordEncoder.encode(usuario.getSenha()));
            }

            if (usuario.getRoles() != null && !usuario.getRoles().isEmpty()) {
                existingUser.setRoles(usuario.getRoles());
            }

            return usuarioRepository.save(existingUser);
        });
    }

    public Optional<Usuario> atualizarAtributo(Usuario usuario) {
        return atualizar(usuario);
    }

    public Optional<UsuarioDTO> autenticarUsuario(UsuarioLoginDTO usuarioLoginDTO) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuarioLoginDTO.email(), usuarioLoginDTO.senha()));

        Usuario usuario = usuarioRepository.findByEmail(usuarioLoginDTO.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        String token = "Bearer " + jwtService.generateToken(usuario.getEmail(), usuario.getRoles());

        return Optional.of(new UsuarioDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                token,
                usuario.getRoles()
        ));
    }

    public void deletar(Long id, String email) {
        Usuario usuarioAutenticado = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário autenticado não encontrado"));

        Usuario usuarioParaExcluir = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("ID: " + id));

        if (usuarioParaExcluir.getEmail().equals(adminEmail)) {
            throw new AcessoNegadoException("O usuário ADMIN padrão não pode ser excluído.");
        }

        boolean isAdmin = usuarioAutenticado.getRoles().contains(Role.ADMIN);
        boolean isProprioUsuario = usuarioAutenticado.getId().equals(usuarioParaExcluir.getId());

        if (isAdmin || isProprioUsuario) {
            usuarioRepository.delete(usuarioParaExcluir);
        } else {
            throw new AcessoNegadoException("Você não tem permissão para excluir este usuário.");
        }
    }


    private UsuarioRequestDTO mapearParaDTO(Usuario usuario) {
        return new UsuarioRequestDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRoles()
        );
    }

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário com ID " + id + " não localizado!"));
    }

    private void atualizarDadosUsuario(Usuario usuario, UsuarioRequestDTO dto) {
        if (dto.nome() != null) usuario.setNome(dto.nome());
        if (dto.email() != null) usuario.setEmail(dto.email());
    }

    private void verificarSeEmailJaExiste(String email) {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("E-mail cadastrado para outro usuário");
        }
    }

    private boolean usuarioDesejaSerAdmin(Usuario usuario) {
        return usuario.getRoles().contains(Role.ADMIN);
    }

    private void validarCriacaoDeAdmin(Authentication authentication) {
        Usuario usuarioLogado = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário autenticado não encontrado"));

        if (!usuarioLogado.getRoles().contains(Role.ADMIN)) {
            throw new IllegalArgumentException("Apenas ADMIN pode criar outro ADMIN!");
        }
    }

    private void atribuirRolePadraoSeNecessario(Usuario usuario) {
        if (usuario.getRoles().isEmpty()) {
            usuario.getRoles().add(Role.USER);
        }
    }
}
