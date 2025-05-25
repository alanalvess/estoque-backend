package com.projetointegrador.estoque.service;

import com.projetointegrador.estoque.dto.*;
import com.projetointegrador.estoque.enums.Role;
import com.projetointegrador.estoque.enums.UnidadeMedida;
import com.projetointegrador.estoque.exeption.AcessoNegadoException;
import com.projetointegrador.estoque.model.*;
import com.projetointegrador.estoque.repository.*;
import com.projetointegrador.estoque.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    private final String ADMIN_EMAIL = "admin@email.com";

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(usuarioService, "adminEmail", ADMIN_EMAIL);
        ReflectionTestUtils.setField(usuarioService, "adminPassword", "admin123");
        ReflectionTestUtils.setField(usuarioService, "adminName", "Administrador");
    }

    @Test
    void criarAdminPadrao_DeveSalvarQuandoNaoExiste() {
        when(usuarioRepository.findByEmail(ADMIN_EMAIL)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("senhaCriptografada");

        usuarioService.criarAdminPadrao();

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void listarTodos_DeveRetornarUsuarios_SeAdminAutenticado() {
        when(usuarioRepository.findAll()).thenReturn(List.of(new Usuario(1L, "João", "joao@email.com", "123", Set.of(Role.USER))));

        ReflectionTestUtils.setField(usuarioService, "adminEmail", "admin@email.com");

        List<UsuarioRequestDTO> usuarios = usuarioService.listarTodos("admin@email.com");

        assertEquals(1, usuarios.size());
        assertEquals("João", usuarios.get(0).nome());
    }

    @Test
    void listarTodos_DeveLancarExcecao_SeNaoForAdmin() {
        String email = "usuario@email.com";

        AcessoNegadoException ex = assertThrows(AcessoNegadoException.class,
                () -> usuarioService.listarTodos(email));

        assertEquals("Acesso Negado: Apenas o ADMIN pode visualizar todos os usuários.", ex.getMessage());
    }

    @Test
    void cadastrar_DeveSalvarUsuarioComSenhaCriptografadaERoleUser() {
        Usuario usuario = new Usuario(null, "Novo Usuário", "novo@email.com", "123456", new HashSet<>());

        when(usuarioRepository.findByEmail("novo@email.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456")).thenReturn("senhaSegura");
        when(usuarioRepository.save(any())).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });

        UsuarioDTO dto = usuarioService.cadastrar(usuario, null);

        assertEquals("Novo Usuário", dto.nome());
        assertEquals("novo@email.com", dto.email());
        assertTrue(dto.roles().contains(Role.USER));
    }

    @Test
    void autenticarUsuario_DeveRetornarUsuarioDTOComToken_SeCredenciaisValidas() {
        Usuario usuario = new Usuario(1L, "Maria", "maria@email.com", "123", Set.of(Role.USER));
        UsuarioLoginDTO login = new UsuarioLoginDTO("maria@email.com", "123");

        when(usuarioRepository.findByEmail("maria@email.com")).thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(anyString(), anySet())).thenReturn("token123");

        Optional<UsuarioDTO> resultado = usuarioService.autenticarUsuario(login);

        assertTrue(resultado.isPresent());
        assertEquals("Maria", resultado.get().nome());
        assertEquals("Bearer token123", resultado.get().token());
    }
}
