package com.projetointegrador.estoque.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetointegrador.estoque.dto.CategoriaDTO;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
class CategoriaControllerIntegrationTest {

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

        // Criação do usuário de teste
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

    private CategoriaDTO cadastrarCategoria(String nome) throws Exception {
        String token = gerarTokenAutenticado();
        CategoriaDTO dto = new CategoriaDTO(null, nome);
        MvcResult result = mockMvc.perform(post("/categorias/cadastrar")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(), CategoriaDTO.class);
    }

    @Test
    void listarTodas_DeveRetornarStatusOk() throws Exception {
        String token = gerarTokenAutenticado();

        mockMvc.perform(get("/categorias/all")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void cadastrar_DeveRetornarStatusCreated() throws Exception {
        String token = gerarTokenAutenticado();
        CategoriaDTO dto = new CategoriaDTO(null, "Limpeza");

        mockMvc.perform(post("/categorias/cadastrar")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Limpeza"));
    }

    @Test
    void buscarPorId_DeveRetornarCategoria() throws Exception {
        String token = gerarTokenAutenticado();
        CategoriaDTO categoria = cadastrarCategoria("Informática");

        mockMvc.perform(get("/categorias/" + categoria.id())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Informática"));
    }

    @Test
    void buscarPorNome_DeveRetornarCategorias() throws Exception {
        String token = gerarTokenAutenticado();
        cadastrarCategoria("Esportes");

        mockMvc.perform(get("/categorias/buscar/Esportes")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Esportes"));
    }

    @Test
    void atualizar_DeveRetornarCategoriaAtualizada() throws Exception {
        String token = gerarTokenAutenticado();
        CategoriaDTO categoria = cadastrarCategoria("Casa");

        CategoriaDTO atualizada = new CategoriaDTO(null, "Casa & Jardim");

        mockMvc.perform(put("/categorias/" + categoria.id())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Casa & Jardim"));
    }

    @Test
    void atualizarAtributo_DeveRetornarCategoriaAtualizadaParcialmente() throws Exception {
        String token = gerarTokenAutenticado();
        CategoriaDTO categoria = cadastrarCategoria("Tecnologia");

        CategoriaDTO parcial = new CategoriaDTO(null, "Tech");

        mockMvc.perform(patch("/categorias/" + categoria.id())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(parcial)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Tech"));
    }

    @Test
    void deletar_DeveRetornarNoContent() throws Exception {
        String token = gerarTokenAutenticado();
        CategoriaDTO categoria = cadastrarCategoria("Roupas");

        mockMvc.perform(delete("/categorias/" + categoria.id())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

}
