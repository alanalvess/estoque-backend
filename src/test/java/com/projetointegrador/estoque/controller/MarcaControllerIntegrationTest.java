package com.projetointegrador.estoque.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetointegrador.estoque.dto.MarcaDTO;
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
class MarcaControllerIntegrationTest {

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

    @Value("${admin.name}")
    private String adminName;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @BeforeEach
    void setup() {
        usuarioRepository.deleteAll();
        Usuario usuario = new Usuario();
        usuario.setNome(adminName);
        usuario.setEmail(adminEmail);
        usuario.setSenha(passwordEncoder.encode(adminPassword));
        usuario.setRoles(Set.of(Role.ADMIN));
        usuarioRepository.save(usuario);
    }

    private String gerarToken() {
        return jwtService.generateToken(adminEmail, Set.of(Role.ADMIN));
    }

    private MarcaDTO cadastrarMarca(String nome) throws Exception {
        String token = gerarToken();
        MarcaDTO dto = new MarcaDTO(null, nome);
        MvcResult result = mockMvc.perform(post("/marcas/cadastrar")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(), MarcaDTO.class);
    }

    @Test
    void listarTodas_DeveRetornarStatusOk() throws Exception {
        String token = gerarToken();
        mockMvc.perform(get("/marcas/all")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void cadastrar_DeveRetornarStatusCreated() throws Exception {
        String token = gerarToken();
        MarcaDTO dto = new MarcaDTO(null, "Marca X");

        mockMvc.perform(post("/marcas/cadastrar")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Marca X"));
    }

    @Test
    void buscarPorId_DeveRetornarMarca() throws Exception {
        String token = gerarToken();
        MarcaDTO marca = cadastrarMarca("Marca Y");

        mockMvc.perform(get("/marcas/" + marca.id())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Marca Y"));
    }

    @Test
    void buscarPorNome_DeveRetornarMarca() throws Exception {
        String token = gerarToken();
        cadastrarMarca("Marca Z");

        mockMvc.perform(get("/marcas/buscar/Marca Z")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Marca Z"));
    }

    @Test
    void atualizar_DeveRetornarMarcaAtualizada() throws Exception {
        String token = gerarToken();
        MarcaDTO marca = cadastrarMarca("Marca Antiga");

        MarcaDTO atualizada = new MarcaDTO(null, "Marca Atualizada");

        mockMvc.perform(put("/marcas/" + marca.id())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Marca Atualizada"));
    }

    @Test
    void atualizarAtributo_DeveAtualizarParcialmente() throws Exception {
        String token = gerarToken();
        MarcaDTO marca = cadastrarMarca("Marca Original");

        MarcaDTO parcial = new MarcaDTO(null, "Marca Parcial");

        mockMvc.perform(patch("/marcas/" + marca.id())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(parcial)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Marca Parcial"));
    }

    @Test
    void deletar_DeveRetornarNoContent() throws Exception {
        String token = gerarToken();
        MarcaDTO marca = cadastrarMarca("Marca para Deletar");

        mockMvc.perform(delete("/marcas/" + marca.id())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }
}