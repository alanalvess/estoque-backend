package com.projetointegrador.estoque.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetointegrador.estoque.dto.CategoriaDTO;
import com.projetointegrador.estoque.dto.ProdutoDTO;
import com.projetointegrador.estoque.enums.Role;
import com.projetointegrador.estoque.enums.UnidadeMedida;
import com.projetointegrador.estoque.model.Categoria;
import com.projetointegrador.estoque.model.Fornecedor;
import com.projetointegrador.estoque.model.Marca;
import com.projetointegrador.estoque.model.Usuario;
import com.projetointegrador.estoque.repository.CategoriaRepository;
import com.projetointegrador.estoque.repository.FornecedorRepository;
import com.projetointegrador.estoque.repository.MarcaRepository;
import com.projetointegrador.estoque.repository.UsuarioRepository;
import com.projetointegrador.estoque.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
class ProdutoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Value("${admin.name}")
    private String adminName;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    private Categoria categoria;
    private Marca marca;
    private Fornecedor fornecedor;

    @BeforeEach
    void setup() {
        usuarioRepository.deleteAll();
        categoriaRepository.deleteAll();
        marcaRepository.deleteAll();
        fornecedorRepository.deleteAll();

        // Criar usuário admin
        Usuario usuario = new Usuario();
        usuario.setNome(adminName);
        usuario.setEmail(adminEmail);
        usuario.setSenha(passwordEncoder.encode(adminPassword));
        usuario.setRoles(Set.of(Role.ADMIN));
        usuarioRepository.save(usuario);

        // Criar dados básicos necessários para produto
        categoria = categoriaRepository.save(Categoria.builder()
                .nome("Categoria Teste")
                .build());

        marca = marcaRepository.save(Marca.builder()
                .nome("Marca Teste")
                .build());
        fornecedor = fornecedorRepository.save(Fornecedor.builder()
                .nome("Fornecedor")
                .cnpj("123456131331")
                .email("email@email.com")
                .telefone("123456789")
                .endereco("rua do teste")
                .build());
    }

    private String gerarToken() {
        return jwtService.generateToken(adminEmail, Set.of(Role.ADMIN));
    }

    private ProdutoDTO criarProdutoDTO(String nome) {
        return new ProdutoDTO(
                null,
                nome,
                "Descrição do produto",
                new BigDecimal("100.00"),
                10,
                true,
                UnidadeMedida.CAIXA, // Exemplo, ajuste conforme seu enum
                "COD123",
                1,
                100,
                LocalDate.now().plusMonths(6),
                LocalDate.now(),
                null,
                categoria,
                marca,
                fornecedor
        );
    }

    private ProdutoDTO cadastrarProduto(String nome) throws Exception {
        String token = gerarToken();
        ProdutoDTO dto = criarProdutoDTO(nome);
        MvcResult result = mockMvc.perform(post("/produtos/cadastrar")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(), ProdutoDTO.class);
    }

    @Test
    void listarTodos_DeveRetornarStatusOk() throws Exception {
        String token = gerarToken();
        mockMvc.perform(get("/produtos/all")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void cadastrar_DeveRetornarStatusCreated() throws Exception {
        String token = gerarToken();
        ProdutoDTO dto = criarProdutoDTO("Produto X");

        mockMvc.perform(post("/produtos/cadastrar")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Produto X"));
    }

    @Test
    void buscarPorId_DeveRetornarProduto() throws Exception {
        String token = gerarToken();
        ProdutoDTO produto = cadastrarProduto("Produto Y");

        mockMvc.perform(get("/produtos/" + produto.id())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Produto Y"));
    }

    @Test
    void buscarPorNome_DeveRetornarProduto() throws Exception {
        String token = gerarToken();
        cadastrarProduto("Produto Z");

        mockMvc.perform(get("/produtos/buscar/Produto Z")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Produto Z"));
    }

    @Test
    void atualizar_DeveRetornarProdutoAtualizado() throws Exception {
        String token = gerarToken();
        ProdutoDTO produto = cadastrarProduto("Produto Antigo");

        ProdutoDTO atualizado = criarProdutoDTO("Produto Atualizado");

        mockMvc.perform(put("/produtos/" + produto.id())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Produto Atualizado"));
    }

    @Test
    void atualizarAtributo_DeveAtualizarParcialmente() throws Exception {
        String token = gerarToken();
        ProdutoDTO produto = cadastrarProduto("Produto Original");

        ProdutoDTO parcial = criarProdutoDTO("Produto Parcial");

        mockMvc.perform(patch("/produtos/" + produto.id())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(parcial)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Produto Parcial"));
    }

    @Test
    void deletar_DeveRetornarNoContent() throws Exception {
        String token = gerarToken();
        ProdutoDTO produto = cadastrarProduto("Produto para Deletar");

        mockMvc.perform(delete("/produtos/" + produto.id())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }
}

