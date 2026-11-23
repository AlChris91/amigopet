package com.amigopet.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.amigopet.api.model.Tutor;
import com.amigopet.api.repository.TutorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TutorRepository tutorRepository;

    private String json(Object valor) throws Exception {
        return objectMapper.writeValueAsString(valor);
    }

    @Test
    @DisplayName("Cadastra tutor e nao devolve a senha na resposta")
    void cadastrarTutor() throws Exception {
        String corpo = json(Map.of(
                "nome", "Maria Silva",
                "email", "maria@email.com",
                "cep", "30123-450",
                "senha", "AmigoPet@2026"));

        mockMvc.perform(post("/api/tutores/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpo))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Maria Silva"))
                .andExpect(jsonPath("$.email").value("maria@email.com"))
                .andExpect(jsonPath("$.senha").doesNotExist());

        Tutor salvo = tutorRepository.findByEmail("maria@email.com").orElseThrow();
        assertThat(salvo.getSenha()).startsWith("$2").doesNotContain("AmigoPet@2026");
    }

    @Test
    @DisplayName("Recusa e-mail ja cadastrado com 409")
    void recusaEmailDuplicado() throws Exception {
        String corpo = json(Map.of(
                "nome", "Maria Silva",
                "email", "maria@email.com",
                "cep", "30123-450",
                "senha", "AmigoPet@2026"));

        mockMvc.perform(post("/api/tutores/cadastro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(corpo))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/tutores/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpo))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensagem").value("Este e-mail ja esta cadastrado!"));
    }

    @Test
    @DisplayName("Recusa cadastro com campos invalidos com 400")
    void recusaCadastroInvalido() throws Exception {
        String corpo = json(Map.of(
                "nome", "",
                "email", "email-invalido",
                "cep", "123",
                "senha", "123"));

        mockMvc.perform(post("/api/tutores/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpo))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").isNotEmpty())
                .andExpect(jsonPath("$.erros.nome").isNotEmpty())
                .andExpect(jsonPath("$.erros.email").isNotEmpty())
                .andExpect(jsonPath("$.erros.cep").isNotEmpty())
                .andExpect(jsonPath("$.erros.senha").isNotEmpty());
    }

    @Test
    @DisplayName("Autentica o tutor e devolve id e nome")
    void loginValido() throws Exception {
        String corpo = json(Map.of(
                "nome", "Maria Silva",
                "email", "maria@email.com",
                "cep", "30123-450",
                "senha", "AmigoPet@2026"));

        mockMvc.perform(post("/api/tutores/cadastro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(corpo))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/tutores/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("email", "maria@email.com", "senha", "AmigoPet@2026"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Maria Silva"))
                .andExpect(jsonPath("$.mensagem").value("Login efetuado com sucesso!"));
    }

    @Test
    @DisplayName("Recusa senha incorreta com 401")
    void loginComSenhaIncorreta() throws Exception {
        mockMvc.perform(post("/api/tutores/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("email", "maria@email.com", "senha", "errada"))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensagem").value("E-mail ou senha invalidos."));
    }

    @Test
    @DisplayName("Atualiza o perfil e mantem a senha quando o campo vem vazio")
    void atualizaPerfil() throws Exception {
        String corpo = json(Map.of(
                "nome", "Maria Silva",
                "email", "maria@email.com",
                "cep", "30123-450",
                "senha", "AmigoPet@2026"));

        mockMvc.perform(post("/api/tutores/cadastro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(corpo))
                .andExpect(status().isCreated());

        Tutor salvo = tutorRepository.findByEmail("maria@email.com").orElseThrow();
        String hashOriginal = salvo.getSenha();

        mockMvc.perform(put("/api/tutores/" + salvo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of(
                                "nome", "Maria Souza",
                                "cep", "30123-450",
                                "senha", ""))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Maria Souza"))
                .andExpect(jsonPath("$.mensagem").value("Perfil do tutor atualizado com sucesso!"));

        Tutor atualizado = tutorRepository.findById(salvo.getId()).orElseThrow();
        assertThat(atualizado.getNome()).isEqualTo("Maria Souza");
        assertThat(atualizado.getSenha()).isEqualTo(hashOriginal);
    }

    @Test
    @DisplayName("Consulta de tutor inexistente devolve 404")
    void buscaTutorInexistente() throws Exception {
        mockMvc.perform(get("/api/tutores/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Tutor nao encontrado."));
    }
}
