package com.amigopet.api.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Solicitacao de mudanca de status de um atendimento pela clinica.
 *
 * @param status novo status: BUSCANDO, CHAMADO, ATENDIDO, CANCELADO ou FINALIZADO
 */
public record AtendimentoStatusDTO(
        @NotBlank(message = "O status do atendimento e obrigatorio.")
        String status) {
}
