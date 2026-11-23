package com.amigopet.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.amigopet.api.model.Clinica;
import com.amigopet.api.model.Servico;
import com.amigopet.api.model.Tutor;
import com.amigopet.api.repository.ClinicaRepository;
import com.amigopet.api.repository.ServicoRepository;
import com.amigopet.api.repository.TutorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AtendimentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClinicaRepository clinicaRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private TutorRepository tutorRepository;

    private Clinica clinica;
    private Tutor tutor;

    @BeforeEach
    void prepararCenario() {
        clinica = new Clinica();
        clinica.setNomeFantasia("Hospital Vet Popular BH");
        clinica.setEmailAdm("adm.vetpopular@email.com");
        clinica.setSenhaAdm("AmigoPet@2026");
        clinica.setCep("31270-010");
        clinica.setEndereco("Av. Antonio Carlos, 6627 - Pampulha");
        clinica.setTelefone("(31) 3499-2222");
        clinica.setLatitude(-19.869123);
        clinica.setLongitude(-43.963456);
        clinica = clinicaRepository.save(clinica);

        Servico servico = new Servico();
        servico.setClinicaId(clinica.getId());
        servico.setNomeServico("Castracao");
        servico.setPreco(new BigDecimal("380.00"));
        servicoRepository.save(servico);

        tutor = new Tutor();
        tutor.setNome("Maria Silva");
        tutor.setEmail("maria@email.com");
        tutor.setCep("30123-450");
        tutor.setSenha("AmigoPet@2026");
        tutor = tutorRepository.save(tutor);
    }

    private String json(Object valor) throws Exception {
        return objectMapper.writeValueAsString(valor);
    }

    @Test
    @DisplayName("Registra entrada na fila com posicao e tempo estimado")
    void registraEntradaNaFila() throws Exception {
        mockMvc.perform(post("/api/atendimentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of(
                                "tutorId", tutor.getId(),
                                "clinicaId", clinica.getId(),
                                "servicoId", 1,
                                "especieAnimal", "Cao",
                                "porteAnimal", "Medio"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.statusAtendimento").value("BUSCANDO"))
                .andExpect(jsonPath("$.posicaoFila").value(1))
                .andExpect(jsonPath("$.tempoEstimadoMinutos").value(15));
    }

    @Test
    @DisplayName("Segunda entrada na fila recebe a segunda posicao")
    void segundaEntradaNaFila() throws Exception {
        Map<String, Object> pedido = Map.of(
                "tutorId", tutor.getId(),
                "clinicaId", clinica.getId(),
                "servicoId", 1,
                "especieAnimal", "Cao",
                "porteAnimal", "Medio");

        mockMvc.perform(post("/api/atendimentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(pedido)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.posicaoFila").value(1));

        mockMvc.perform(post("/api/atendimentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(pedido)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.posicaoFila").value(2))
                .andExpect(jsonPath("$.tempoEstimadoMinutos").value(30));
    }

    @Test
    @DisplayName("Recusa atendimento de tutor inexistente com 404")
    void recusaTutorInexistente() throws Exception {
        mockMvc.perform(post("/api/atendimentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of(
                                "tutorId", 999,
                                "clinicaId", clinica.getId(),
                                "servicoId", 1,
                                "especieAnimal", "Cao",
                                "porteAnimal", "Medio"))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Tutor nao encontrado. Faca login novamente."));
    }

    @Test
    @DisplayName("Recusa atendimento de clinica inexistente com 404")
    void recusaClinicaInexistente() throws Exception {
        mockMvc.perform(post("/api/atendimentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of(
                                "tutorId", tutor.getId(),
                                "clinicaId", 999,
                                "servicoId", 1,
                                "especieAnimal", "Cao",
                                "porteAnimal", "Medio"))))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Recusa atendimento sem os campos obrigatorios com 400")
    void recusaAtendimentoInvalido() throws Exception {
        mockMvc.perform(post("/api/atendimentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of(
                                "tutorId", tutor.getId(),
                                "clinicaId", clinica.getId(),
                                "especieAnimal", "",
                                "porteAnimal", ""))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros.especieAnimal").isNotEmpty())
                .andExpect(jsonPath("$.erros.porteAnimal").isNotEmpty());
    }

    @Test
    @DisplayName("Clinica chama o proximo da fila e a fila e reordenada")
    void chamaProximoDaFila() throws Exception {
        Map<String, Object> pedido = Map.of(
                "tutorId", tutor.getId(),
                "clinicaId", clinica.getId(),
                "servicoId", 1,
                "especieAnimal", "Cao",
                "porteAnimal", "Medio");

        mockMvc.perform(post("/api/atendimentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(pedido)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/atendimentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(pedido)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.posicaoFila").value(2));

        mockMvc.perform(get("/api/atendimentos/clinica/" + clinica.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        mockMvc.perform(put("/api/atendimentos/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("status", "CHAMADO"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusAtendimento").value("CHAMADO"));

        mockMvc.perform(get("/api/atendimentos/clinica/" + clinica.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].posicaoFila").value(1))
                .andExpect(jsonPath("$[0].tempoEstimadoMinutos").value(15));
    }

    @Test
    @DisplayName("Recusa status fora da lista permitida com 400")
    void recusaStatusInvalido() throws Exception {
        mockMvc.perform(post("/api/atendimentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(Map.of(
                        "tutorId", tutor.getId(),
                        "clinicaId", clinica.getId(),
                        "servicoId", 1,
                        "especieAnimal", "Cao",
                        "porteAnimal", "Medio"))))
                .andExpect(status().isCreated());

        mockMvc.perform(put("/api/atendimentos/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("status", "QUALQUER_COISA"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").isNotEmpty());
    }

    @Test
    @DisplayName("Consulta de atendimento inexistente devolve 404")
    void buscaAtendimentoInexistente() throws Exception {
        mockMvc.perform(get("/api/atendimentos/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Atendimento nao encontrado."));
    }
}
