package com.amigopet.api.dto;

import java.time.LocalDateTime;

import com.amigopet.api.model.Atendimento;

/**
 * Atendimento devolvido ao front-end.
 *
 * <p>Mantem os mesmos nomes de campo usados pelo front-end
 * ({@code posicaoFila}, {@code tempoEstimadoMinutos},
 * {@code statusAtendimento}) para preservar o contrato da API.</p>
 *
 * @param id                   identificador do atendimento
 * @param tutorId              tutor da fila
 * @param clinicaId            clinica que executara o atendimento
 * @param servicoId            servico reservado
 * @param especieAnimal        especie do animal
 * @param porteAnimal          porte do animal
 * @param statusAtendimento     situacao atual na fila
 * @param posicaoFila          posicao do tutor na fila
 * @param tempoEstimadoMinutos tempo estimado de espera
 * @param dataRegistro         momento de entrada na fila
 */
public record AtendimentoResponseDTO(
        Long id,
        Long tutorId,
        Long clinicaId,
        Long servicoId,
        String especieAnimal,
        String porteAnimal,
        String statusAtendimento,
        Integer posicaoFila,
        Integer tempoEstimadoMinutos,
        LocalDateTime dataRegistro) {

    public static AtendimentoResponseDTO from(Atendimento atendimento) {
        return new AtendimentoResponseDTO(
                atendimento.getId(),
                atendimento.getTutorId(),
                atendimento.getClinicaId(),
                atendimento.getServicoId(),
                atendimento.getEspecieAnimal(),
                atendimento.getPorteAnimal(),
                atendimento.getStatusAtendimento(),
                atendimento.getPosicaoFila(),
                atendimento.getTempoEstimadoMinutos(),
                atendimento.getDataRegistro());
    }
}
