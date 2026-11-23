package com.amigopet.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import com.amigopet.api.model.OrientacaoServico;
import com.amigopet.api.model.Servico;
import com.amigopet.api.repository.ClinicaRepository;
import com.amigopet.api.repository.OrientacaoServicoRepository;
import com.amigopet.api.repository.ServicoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ServicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClinicaRepository clinicaRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private OrientacaoServicoRepository orientacaoRepository;

    private Servico servico;

    @BeforeEach
    void prepararCenario() {
        Clinica clinica = new Clinica();
        clinica.setNomeFantasia("Hospital Vet Popular BH");
        clinica.setEmailAdm("adm.vetpopular@email.com");
        clinica.setSenhaAdm("AmigoPet@2026");
        clinica.setCep("31270-010");
        clinica.setEndereco("Av. Antonio Carlos, 6627 - Pampulha");
        clinica.setTelefone("(31) 3499-2222");
        clinica.setLatitude(-19.869123);
        clinica.setLongitude(-43.963456);
        clinica = clinicaRepository.save(clinica);

        servico = new Servico();
        servico.setClinicaId(clinica.getId());
        servico.setNomeServico("Castracao");
        servico.setPreco(new BigDecimal("380.00"));
        servico = servicoRepository.save(servico);

        orientacaoRepository.save(new OrientacaoServico(servico.getId(),
                "Jejum hidrico e alimentar absoluto de 8 horas antes do procedimento."));
        orientacaoRepository.save(new OrientacaoServico(servico.getId(),
                "Trazer uma manta ou cobertor para o pos-operatorio do animal."));
    }

    @Test
    @DisplayName("Lista todos os servicos cadastrados")
    void listaTodosOsServicos() throws Exception {
        mockMvc.perform(get("/api/servicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nomeServico").value("Castracao"))
                .andExpect(jsonPath("$[0].preco").value(380.00));
    }

    @Test
    @DisplayName("Cadastra um servico para a clinica")
    void cadastraServico() throws Exception {
        mockMvc.perform(post("/api/servicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "clinicaId", servico.getClinicaId(),
                                "nomeServico", "Consulta Especializada",
                                "preco", 150.00))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nomeServico").value("Consulta Especializada"))
                .andExpect(jsonPath("$.preco").value(150.00));
    }

    @Test
    @DisplayName("Cadastra uma orientacao pre-atendimento para o servico")
    void cadastraOrientacao() throws Exception {
        mockMvc.perform(post("/api/servicos/orientacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "servicoId", servico.getId(),
                                "descricaoOrientacao", "Jejum de 4 horas antes da coleta."))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.servicoId").value(servico.getId()))
                .andExpect(jsonPath("$.descricaoOrientacao").value("Jejum de 4 horas antes da coleta."));

        mockMvc.perform(get("/api/servicos/" + servico.getId() + "/orientacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    @DisplayName("Orientacao com servico inexistente devolve 404")
    void orientacaoComServicoInexistente() throws Exception {
        mockMvc.perform(post("/api/servicos/orientacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "servicoId", 999,
                                "descricaoOrientacao", "Jejum de 4 horas antes da coleta."))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Servico nao encontrado."));
    }

    @Test
    @DisplayName("Remove o servico e as orientacoes vinculadas")
    void excluiServico() throws Exception {
        mockMvc.perform(delete("/api/servicos/" + servico.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/servicos/" + servico.getId() + "/orientacoes"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Lista as orientacoes de um servico")
    void listaOrientacoesDoServico() throws Exception {
        mockMvc.perform(get("/api/servicos/" + servico.getId() + "/orientacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].descricaoOrientacao")
                        .value("Jejum hidrico e alimentar absoluto de 8 horas antes do procedimento."));
    }

    @Test
    @DisplayName("Orientacoes de servico inexistente devolvem 404")
    void orientacoesDeServicoInexistente() throws Exception {
        mockMvc.perform(get("/api/servicos/999/orientacoes"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Servico nao encontrado."));
    }

    @Test
    @DisplayName("Agrupa as orientacoes de todos os servicos de uma clinica")
    void agrupaOrientacoesPorClinica() throws Exception {
        mockMvc.perform(get("/api/servicos/clinica/" + servico.getClinicaId() + "/orientacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$['" + servico.getId() + "'].length()").value(2));
    }
}
