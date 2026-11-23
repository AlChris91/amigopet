package com.amigopet.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import com.amigopet.api.repository.ClinicaRepository;
import com.amigopet.api.repository.ServicoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ClinicaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClinicaRepository clinicaRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    private Clinica clinicaPampulha;
    private Clinica clinicaSantoAgostinho;

    @BeforeEach
    void prepararCenario() {
        clinicaPampulha = new Clinica();
        clinicaPampulha.setNomeFantasia("Hospital Vet Popular BH");
        clinicaPampulha.setEmailAdm("adm.vetpopular@email.com");
        clinicaPampulha.setSenhaAdm("AmigoPet@2026");
        clinicaPampulha.setCep("31270-010");
        clinicaPampulha.setEndereco("Av. Antonio Carlos, 6627 - Pampulha");
        clinicaPampulha.setTelefone("(31) 3499-2222");
        clinicaPampulha.setLatitude(-19.869123);
        clinicaPampulha.setLongitude(-43.963456);
        clinicaPampulha = clinicaRepository.save(clinicaPampulha);

        clinicaSantoAgostinho = new Clinica();
        clinicaSantoAgostinho.setNomeFantasia("Clinica Vet Santo Agostinho");
        clinicaSantoAgostinho.setEmailAdm("adm.santoagostinho@email.com");
        clinicaSantoAgostinho.setSenhaAdm("AmigoPet@2026");
        clinicaSantoAgostinho.setCep("30190-060");
        clinicaSantoAgostinho.setEndereco("Rua Araguari, 1345 - Santo Agostinho");
        clinicaSantoAgostinho.setTelefone("(31) 3291-5555");
        clinicaSantoAgostinho.setLatitude(-19.929845);
        clinicaSantoAgostinho.setLongitude(-43.948123);
        clinicaSantoAgostinho = clinicaRepository.save(clinicaSantoAgostinho);

        servicoRepository.save(criarServico(clinicaPampulha.getId(), "Castracao", "380.00"));
        servicoRepository.save(criarServico(clinicaSantoAgostinho.getId(), "Consulta Geral", "140.00"));
    }

    private Servico criarServico(Long clinicaId, String nome, String preco) {
        Servico servico = new Servico();
        servico.setClinicaId(clinicaId);
        servico.setNomeServico(nome);
        servico.setPreco(new BigDecimal(preco));
        return servicoRepository.save(servico);
    }

    private String json(Object valor) throws Exception {
        return objectMapper.writeValueAsString(valor);
    }

    @Test
    @DisplayName("Busca devolve somente a clinica que oferece o servico pedido")
    void buscaFiltraPorServico() throws Exception {
        mockMvc.perform(get("/api/clinicas/busca")
                        .param("cep", "31270-010")
                        .param("servico", "Castracao")
                        .param("raio", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(clinicaPampulha.getId()))
                .andExpect(jsonPath("$[0].nomeFantasia").value("Hospital Vet Popular BH"))
                .andExpect(jsonPath("$[0].senhaAdm").doesNotExist());
    }

    @Test
    @DisplayName("Busca nao devolve clinicas que nao oferecem o servico pedido")
    void buscaSemClinicasParaOServico() throws Exception {
        mockMvc.perform(get("/api/clinicas/busca")
                        .param("cep", "31270-010")
                        .param("servico", "Vacinacao")
                        .param("raio", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("Busca respeita o raio de distancia informado")
    void buscaFiltraPorRaio() throws Exception {
        mockMvc.perform(get("/api/clinicas/busca")
                        .param("cep", "31270-010")
                        .param("servico", "Consulta")
                        .param("raio", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        mockMvc.perform(get("/api/clinicas/busca")
                        .param("cep", "31270-010")
                        .param("servico", "Consulta")
                        .param("raio", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(clinicaSantoAgostinho.getId()));
    }

    @Test
    @DisplayName("Busca ordena as clinicas da mais proxima para a mais distante")
    void buscaOrdenaPorDistancia() throws Exception {
        mockMvc.perform(get("/api/clinicas/busca")
                        .param("cep", "31270-010")
                        .param("servico", "")
                        .param("raio", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(clinicaPampulha.getId()))
                .andExpect(jsonPath("$[1].id").value(clinicaSantoAgostinho.getId()))
                .andExpect(jsonPath("$[0].distancia").isNumber());
    }

    @Test
    @DisplayName("Busca aceita as coordenadas exatas do tutor")
    void buscaPorCoordenadas() throws Exception {
        mockMvc.perform(get("/api/clinicas/busca")
                        .param("latitude", "-19.869123")
                        .param("longitude", "-43.963456")
                        .param("servico", "Castracao")
                        .param("raio", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].distancia").value(0.0));
    }

    @Test
    @DisplayName("Cadastra clinica e nao devolve a senha administrativa")
    void cadastrarClinica() throws Exception {
        String corpo = json(Map.of(
                "nomeFantasia", "Clinica Nova",
                "emailAdm", "adm.nova@email.com",
                "senhaAdm", "AmigoPet@2026",
                "cep", "30411-000",
                "endereco", "Av. Amazonas, 2560 - Prado",
                "telefone", "(31) 3335-4444",
                "latitude", -19.932412,
                "longitude", -43.958789));

        mockMvc.perform(post("/api/clinicas/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpo))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nomeFantasia").value("Clinica Nova"))
                .andExpect(jsonPath("$.senhaAdm").doesNotExist());

        Clinica salva = clinicaRepository.findByEmailAdm("adm.nova@email.com").orElseThrow();
        assertThat(salva.getSenhaAdm()).startsWith("$2");
    }

    @Test
    @DisplayName("Autentica o administrador mesmo com a senha legada em texto puro")
    void loginComSenhaLegada() throws Exception {
        mockMvc.perform(post("/api/clinicas/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of(
                                "emailAdm", "adm.vetpopular@email.com",
                                "senhaAdm", "AmigoPet@2026"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clinicaPampulha.getId()))
                .andExpect(jsonPath("$.nome").value("Hospital Vet Popular BH"));

        Clinica migrada = clinicaRepository.findByEmailAdm("adm.vetpopular@email.com").orElseThrow();
        assertThat(migrada.getSenhaAdm()).startsWith("$2");
    }

    @Test
    @DisplayName("Recusa e-mail administrativo ja cadastrado com 409")
    void recusaEmailDuplicado() throws Exception {
        mockMvc.perform(post("/api/clinicas/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of(
                                "emailAdm", "adm.vetpopular@email.com",
                                "senhaAdm", "AmigoPet@2026"))))
                .andExpect(status().isOk());

        String corpo = json(Map.of(
                "nomeFantasia", "Outra Clinica",
                "emailAdm", "adm.vetpopular@email.com",
                "senhaAdm", "AmigoPet@2026",
                "cep", "30411-000",
                "endereco", "Av. Amazonas, 2560 - Prado",
                "telefone", "(31) 3335-4444"));

        mockMvc.perform(post("/api/clinicas/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpo))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Lista os servicos de uma clinica")
    void listaServicosDaClinica() throws Exception {
        mockMvc.perform(get("/api/clinicas/" + clinicaPampulha.getId() + "/servicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nomeServico").value("Castracao"));
    }
}
