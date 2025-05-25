package com.projetointegrador.estoque.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetointegrador.estoque.dto.FornecedorDTO;
import com.projetointegrador.estoque.enums.Role;
import com.projetointegrador.estoque.model.Usuario;
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

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
class FornecedorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Value("${admin.name}")
    private String adminName;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @BeforeEach
    void setup() {
        repository.deleteAll();
        Usuario usuario = new Usuario();
        usuario.setNome(adminName);
        usuario.setEmail(adminEmail);
        usuario.setSenha(passwordEncoder.encode(adminPassword));
        usuario.setRoles(Set.of(Role.ADMIN));
        repository.save(usuario);
    }

    private String gerarTokenAutenticado() {
        return jwtService.generateToken(adminEmail, Set.of(Role.ADMIN));
    }

    private FornecedorDTO cadastrarFornecedor(String nome, String cnpj, String email, String telefone, String endereco) throws Exception {
        String token = gerarTokenAutenticado();
        FornecedorDTO dto = new FornecedorDTO(null, nome, cnpj, email, telefone, endereco);
        MvcResult result = mockMvc.perform(post("/fornecedores/cadastrar")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(), FornecedorDTO.class);
    }

    @Test
    void listarTodos_DeveRetornarStatusOk() throws Exception {
        String token = gerarTokenAutenticado();
        mockMvc.perform(get("/fornecedores/all")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void cadastrar_DeveRetornarStatusCreated() throws Exception {
        String token = gerarTokenAutenticado();
        FornecedorDTO dto = new FornecedorDTO(null, "Fornecedor X", "12345678000199", "fornecedorx@email.com", "11999999999", "Rua A, 123");

        mockMvc.perform(post("/fornecedores/cadastrar")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Fornecedor X"))
                .andExpect(jsonPath("$.email").value("fornecedorx@email.com"))
                .andExpect(jsonPath("$.telefone").value("11999999999"))
                .andExpect(jsonPath("$.endereco").value("Rua A, 123"));
    }

    @Test
    void buscarPorId_DeveRetornarFornecedor() throws Exception {
        String token = gerarTokenAutenticado();
        FornecedorDTO fornecedor = cadastrarFornecedor("Fornecedor Y", "98765432000100", "fory@email.com", "11988888888", "Rua B, 456");

        mockMvc.perform(get("/fornecedores/" + fornecedor.id())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Fornecedor Y"));
    }

    @Test
    void buscarPorNome_DeveRetornarFornecedor() throws Exception {
        String token = gerarTokenAutenticado();
        cadastrarFornecedor("Fornecedor Z", "11111111000111", "forz@email.com", "11977777777", "Rua C, 789");

        mockMvc.perform(get("/fornecedores/buscar/nome/Fornecedor Z")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Fornecedor Z"));
    }

    @Test
    void buscarPorCNPJ_DeveRetornarFornecedor() throws Exception {
        String token = gerarTokenAutenticado();
        cadastrarFornecedor("Fornecedor ABC", "22222222000122", "forabc@email.com", "11966666666", "Rua D, 101");

        mockMvc.perform(get("/fornecedores/buscar/cnpj/22222222000122")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Fornecedor ABC"));
    }

    @Test
    void atualizar_DeveRetornarFornecedorAtualizado() throws Exception {
        String token = gerarTokenAutenticado();
        FornecedorDTO fornecedor = cadastrarFornecedor("Fornecedor Antigo", "33333333000133", "oldfor@email.com", "11955555555", "Rua E, 202");

        FornecedorDTO atualizado = new FornecedorDTO(null, "Fornecedor Novo", "33333333000133", "newfor@email.com", "11955555555", "Rua E, 202");

        mockMvc.perform(put("/fornecedores/" + fornecedor.id())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Fornecedor Novo"))
                .andExpect(jsonPath("$.email").value("newfor@email.com"));
    }

    @Test
    void atualizarAtributo_DeveAtualizarParcialmente() throws Exception {
        String token = gerarTokenAutenticado();
        FornecedorDTO fornecedor = cadastrarFornecedor("Fornecedor Parcial", "44444444000144", "partialfor@email.com", "11944444444", "Rua F, 303");

        FornecedorDTO parcial = new FornecedorDTO(null, "Fornecedor Atualizado", null, null, null, null);

        mockMvc.perform(patch("/fornecedores/" + fornecedor.id())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(parcial)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Fornecedor Atualizado"));
    }

    @Test
    void deletar_DeveRetornarNoContent() throws Exception {
        String token = gerarTokenAutenticado();
        FornecedorDTO fornecedor = cadastrarFornecedor("Fornecedor Delete", "55555555000155", "deletefor@email.com", "11933333333", "Rua G, 404");

        mockMvc.perform(delete("/fornecedores/" + fornecedor.id())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }
}
