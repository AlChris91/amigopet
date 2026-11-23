package com.amigopet.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Dados enviados pela clinica parceira para cadastrar uma orientacao
 * pre-atendimento.
 *
 * @param servicoId          servico a que a orientacao pertence
 * @param descricaoOrientacao texto exibido ao tutor
 */
public record OrientacaoCadastroDTO(
        @NotNull(message = "O servico da orientacao e obrigatorio.")
        Long servicoId,

        @NotBlank(message = "A descricao da orientacao e obrigatoria.")
        @Size(max = 255, message = "A descricao deve ter no maximo 255 caracteres.")
        String descricaoOrientacao) {
}
