package com.amigopet.api.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amigopet.api.dto.OrientacaoCadastroDTO;
import com.amigopet.api.dto.OrientacaoResponseDTO;
import com.amigopet.api.dto.ServicoCadastroDTO;
import com.amigopet.api.dto.ServicoResponseDTO;
import com.amigopet.api.exception.ApiException;
import com.amigopet.api.model.OrientacaoServico;
import com.amigopet.api.model.Servico;
import com.amigopet.api.repository.ClinicaRepository;
import com.amigopet.api.repository.OrientacaoServicoRepository;
import com.amigopet.api.repository.ServicoRepository;

/**
 * Regras de negocio dos servicos oferecidos pelas clinicas e das orientacoes
 * pre-atendimento exibidas na confirmacao do agendamento.
 */
@Service
public class ServicoServico {

    private final ServicoRepository servicoRepository;
    private final OrientacaoServicoRepository orientacaoRepository;
    private final ClinicaRepository clinicaRepository;

    public ServicoServico(ServicoRepository servicoRepository,
                          OrientacaoServicoRepository orientacaoRepository,
                          ClinicaRepository clinicaRepository) {
        this.servicoRepository = servicoRepository;
        this.orientacaoRepository = orientacaoRepository;
        this.clinicaRepository = clinicaRepository;
    }

    /**
     * Lista todos os servicos cadastrados.
     *
     * @return servicos com aclinica e o preco
     */
    @Transactional(readOnly = true)
    public List<ServicoResponseDTO> listarTodos() {
        return servicoRepository.findAll().stream()
                .map(ServicoResponseDTO::from)
                .collect(Collectors.toList());
    }

    /**
     * Lista os servicos de uma clinica.
     *
     * @param clinicaId identificador da clinica
     * @return servicos da clinica
     */
    @Transactional(readOnly = true)
    public List<ServicoResponseDTO> listarPorClinica(Long clinicaId) {
        return servicoRepository.findByClinicaIdOrderByNomeServicoAsc(clinicaId).stream()
                .map(ServicoResponseDTO::from)
                .collect(Collectors.toList());
    }

    /**
     * Cadastra um servico para uma clinica parceira.
     *
     * @param dados dados validados do servico
     * @return o servico cadastrado
     */
    @Transactional
    public ServicoResponseDTO cadastrar(ServicoCadastroDTO dados) {
        if (!clinicaRepository.existsById(dados.clinicaId())) {
            throw ApiException.notFound("Clinica nao encontrada.");
        }

        Servico servico = new Servico();
        servico.setClinicaId(dados.clinicaId());
        servico.setNomeServico(dados.nomeServico().trim());
        servico.setPreco(dados.preco());
        servico.setEspecieAtendida(dados.especieAtendida());          
        servico.setOrientacaoPreAtendimento(dados.orientacaoPreAtendimento()); 
        return ServicoResponseDTO.from(servicoRepository.save(servico));
    }

    /**
     * Remove um servico e as orientacoes vinculadas a ele.
     *
     * @param id identificador do servico
     */
    @Transactional
    public void excluir(Long id) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Servico nao encontrado."));

        orientacaoRepository.deleteAll(orientacaoRepository.findByServicoIdOrderByIdAsc(id));
        servicoRepository.delete(servico);
    }

    /**
     * Cadastra uma orientacao pre-atendimento para um servico.
     *
     * @param dados dados validados da orientacao
     * @return a orientacao cadastrada
     */
    @Transactional
    public OrientacaoResponseDTO cadastrarOrientacao(OrientacaoCadastroDTO dados) {
        if (!servicoRepository.existsById(dados.servicoId())) {
            throw ApiException.notFound("Servico nao encontrado.");
        }

        OrientacaoServico orientacao = new OrientacaoServico(
                dados.servicoId(), dados.descricaoOrientacao().trim());

        return OrientacaoResponseDTO.from(orientacaoRepository.save(orientacao));
    }

    /**
     * Lista as orientacoes de um servico.
     *
     * @param servicoId identificador do servico
     * @return orientacoes do servico
     */
    @Transactional(readOnly = true)
    public List<OrientacaoResponseDTO> listarOrientacoes(Long servicoId) {
        if (!servicoRepository.existsById(servicoId)) {
            throw ApiException.notFound("Servico nao encontrado.");
        }

        return orientacaoRepository.findByServicoIdOrderByIdAsc(servicoId).stream()
                .map(OrientacaoResponseDTO::from)
                .collect(Collectors.toList());
    }

    /**
     * Lista as orientacoes de todos os servicos de uma clinica, agrupadas por servico.
     *
     * @param clinicaId identificador da clinica
     * @return orientacoes agrupadas pelo identificador do servico
     */
    @Transactional(readOnly = true)
    public Map<Long, List<OrientacaoResponseDTO>> listarOrientacoesPorClinica(Long clinicaId) {
        List<Long> servicoIds = servicoRepository.findByClinicaIdOrderByNomeServicoAsc(clinicaId).stream()
                .map(Servico::getId)
                .collect(Collectors.toList());

        if (servicoIds.isEmpty()) {
            return Map.of();
        }

        Map<Long, List<OrientacaoResponseDTO>> agrupadas = new LinkedHashMap<>();
        for (OrientacaoServico orientacao : orientacaoRepository.findByServicoIdIn(servicoIds)) {
            agrupadas.computeIfAbsent(orientacao.getServicoId(), chave -> new java.util.ArrayList<>())
                    .add(OrientacaoResponseDTO.from(orientacao));
        }
        return agrupadas;
    }
}
