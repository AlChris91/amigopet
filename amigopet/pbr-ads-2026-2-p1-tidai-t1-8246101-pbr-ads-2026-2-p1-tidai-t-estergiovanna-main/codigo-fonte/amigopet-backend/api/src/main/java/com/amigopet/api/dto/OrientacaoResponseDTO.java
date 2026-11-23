package com.amigopet.api.dto;

import com.amigopet.api.model.OrientacaoServico;

/**
 * Orientacao pre-atendimento exibida na confirmacao do agendamento.
 *
 * @param id                 identificador da orientacao
 * @param servicoId          servico a que a orientacao pertence
 * @param descricaoOrientacao texto exibido ao tutor
 */
public record OrientacaoResponseDTO(Long id, Long servicoId, String descricaoOrientacao) {

    public static OrientacaoResponseDTO from(OrientacaoServico orientacao) {
        return new OrientacaoResponseDTO(
                orientacao.getId(),
                orientacao.getServicoId(),
                orientacao.getDescricaoOrientacao());
    }
}
