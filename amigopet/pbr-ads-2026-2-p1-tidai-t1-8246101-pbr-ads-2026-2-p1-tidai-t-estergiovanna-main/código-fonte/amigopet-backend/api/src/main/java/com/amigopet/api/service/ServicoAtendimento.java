package com.amigopet.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amigopet.api.dto.AtendimentoRequestDTO;
import com.amigopet.api.dto.AtendimentoResponseDTO;
import com.amigopet.api.exception.ApiException;
import com.amigopet.api.model.Atendimento;
import com.amigopet.api.model.Servico;
import com.amigopet.api.model.StatusAtendimento;
import com.amigopet.api.repository.AtendimentoRepository;
import com.amigopet.api.repository.ClinicaRepository;
import com.amigopet.api.repository.ServicoRepository;
import com.amigopet.api.repository.TutorRepository;

/**
 * Regras de negocio da fila virtual de atendimentos.
 */
@Service
public class ServicoAtendimento {

    /** Tempo medio de atendimento de um pet, usado para estimar a espera. */
    private static final int MINUTOS_POR_PET = 15;

    private final AtendimentoRepository atendimentoRepository;
    private final ClinicaRepository clinicaRepository;
    private final ServicoRepository servicoRepository;
    private final TutorRepository tutorRepository;

    public ServicoAtendimento(AtendimentoRepository atendimentoRepository,
                              ClinicaRepository clinicaRepository,
                              ServicoRepository servicoRepository,
                              TutorRepository tutorRepository) {
        this.atendimentoRepository = atendimentoRepository;
        this.clinicaRepository = clinicaRepository;
        this.servicoRepository = servicoRepository;
        this.tutorRepository = tutorRepository;
    }

    /**
     * Registra a entrada do tutor na fila da clinica escolhida.
     *
     * <p>O status, a posicao e o tempo estimado sao definidos pelo servidor,
     * nunca pelo front-end. Quando o servico enviado nao pertence a clinica
     * selecionada, o primeiro servico da clinica e utilizado, mantendo o
     * fluxo do aplicativo funcionando mesmo com o campo fixo no front-end.</p>
     *
     * @param dados dados da solicitacao
     * @return o atendimento registrado com a posicao na fila
     */
    @Transactional
    public AtendimentoResponseDTO registrar(AtendimentoRequestDTO dados) {
        if (!tutorRepository.existsById(dados.tutorId())) {
            throw ApiException.notFound("Tutor nao encontrado. Faca login novamente.");
        }

        if (!clinicaRepository.existsById(dados.clinicaId())) {
            throw ApiException.notFound("Clinica nao encontrada.");
        }

        Long servicoId = resolverServico(dados.clinicaId(), dados.servicoId());

        List<Atendimento> ativos = atendimentoRepository.findByClinicaIdAndStatusAtendimento(
                dados.clinicaId(), StatusAtendimento.BUSCANDO);

        Atendimento atendimento = new Atendimento();
        atendimento.setTutorId(dados.tutorId());
        atendimento.setClinicaId(dados.clinicaId());
        atendimento.setServicoId(servicoId);
        atendimento.setEspecieAnimal(dados.especieAnimal().trim());
        atendimento.setPorteAnimal(dados.porteAnimal().trim());
        atendimento.setStatus(StatusAtendimento.BUSCANDO);
        atendimento.setPosicaoFila(ativos.size() + 1);
        atendimento.setTempoEstimadoMinutos((ativos.size() + 1) * MINUTOS_POR_PET);

        return AtendimentoResponseDTO.from(atendimentoRepository.save(atendimento));
    }

    /**
     * Lista a fila de atendimento de uma clinica, na ordem de chegada.
     *
     * @param clinicaId identificador da clinica
     * @return atendimentos aguardando
     */
    @Transactional(readOnly = true)
    public List<AtendimentoResponseDTO> listarFila(Long clinicaId) {
        return atendimentoRepository
                .findByClinicaIdAndStatusAtendimentoOrderByDataRegistroAscIdAsc(clinicaId, StatusAtendimento.BUSCANDO)
                .stream()
                .map(AtendimentoResponseDTO::from)
                .collect(Collectors.toList());
    }

    /**
     * Busca um atendimento pelo identificador.
     *
     * @param id identificador do atendimento
     * @return o atendimento
     */
    @Transactional(readOnly = true)
    public AtendimentoResponseDTO buscarPorId(Long id) {
        return AtendimentoResponseDTO.from(obterEntidade(id));
    }

    /**
     * Altera o status de um atendimento e reorganiza a fila da clinica.
     *
     * @param id     identificador do atendimento
     * @param status novo status informado pela clinica
     * @return o atendimento ja atualizado
     */
    @Transactional
    public AtendimentoResponseDTO atualizarStatus(Long id, String status) {
        if (!StatusAtendimento.isValido(status)) {
            throw ApiException.badRequest("Status invalido. Use: BUSCANDO, CHAMADO, ATENDIDO, "
                    + "CANCELADO ou FINALIZADO.");
        }

        Atendimento atendimento = obterEntidade(id);
        atendimento.setStatusAtendimento(status);

        if (atendimento.getStatus() != StatusAtendimento.BUSCANDO) {
            atendimento.setPosicaoFila(0);
            atendimento.setTempoEstimadoMinutos(0);
        }

        atendimentoRepository.save(atendimento);
        recalcularFila(atendimento.getClinicaId());

        return AtendimentoResponseDTO.from(atendimento);
    }

    /**
     * Recalcula posicao e tempo estimado de todos os atendimentos da fila.
     *
     * @param clinicaId identificador da clinica
     */
    private void recalcularFila(Long clinicaId) {
        List<Atendimento> fila = atendimentoRepository
                .findByClinicaIdAndStatusAtendimentoOrderByDataRegistroAscIdAsc(clinicaId, StatusAtendimento.BUSCANDO);

        for (int i = 0; i < fila.size(); i++) {
            fila.get(i).setPosicaoFila(i + 1);
            fila.get(i).setTempoEstimadoMinutos((i + 1) * MINUTOS_POR_PET);
        }
        atendimentoRepository.saveAll(fila);
    }

    /**
     * Define qual servico sera reservado no atendimento.
     *
     * @param clinicaId  clinica escolhida
     * @param servicoId  servico enviado pelo front-end, pode ser nulo
     * @return identificador do servico valido para a clinica
     */
    private Long resolverServico(Long clinicaId, Long servicoId) {
        List<Servico> servicosDaClinica = servicoRepository.findByClinicaIdOrderByNomeServicoAsc(clinicaId);

        if (servicosDaClinica.isEmpty()) {
            throw ApiException.badRequest("A clinica escolhida ainda nao possui servicos cadastrados.");
        }

        if (servicoId != null) {
            boolean pertence = servicosDaClinica.stream()
                    .anyMatch(servico -> servico.getId().equals(servicoId));
            if (pertence) {
                return servicoId;
            }
        }

        return servicosDaClinica.get(0).getId();
    }

    /**
     * Obtem a entidade do atendimento, Lancando erro quando ela nao existe.
     *
     * @param id identificador do atendimento
     * @return entidade do atendimento
     */
    private Atendimento obterEntidade(Long id) {
        return atendimentoRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Atendimento nao encontrado."));
    }
}
