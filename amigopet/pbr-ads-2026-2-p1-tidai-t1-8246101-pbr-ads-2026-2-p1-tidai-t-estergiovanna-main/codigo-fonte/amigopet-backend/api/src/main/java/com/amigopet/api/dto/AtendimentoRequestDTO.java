package com.amigopet.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Solicitacao de entrada na fila virtual.
 *
 * @param tutorId       tutor que esta requesting o atendimento
 * @param clinicaId     clinica escolhida na busca
 * @param servicoId     servico desejado, opcional: quando ausente ou invalido
 *                      o back-end usa o primeiro servico da clinica
 * @param especieAnimal especie do animal ("Cao", "Gato")
 * @param porteAnimal   porte do animal ("Pequeno", "Medio", "Grande")
 */
public record AtendimentoRequestDTO(
        @NotNull(message = "O tutor do atendimento e obrigatorio.")
        Long tutorId,

        @NotNull(message = "A clinica do atendimento e obrigatoria.")
        Long clinicaId,

        Long servicoId,

        @NotBlank(message = "Informe a especie do animal.")
        @Size(max = 30, message = "A especie deve ter no maximo 30 caracteres.")
        String especieAnimal,

        @NotBlank(message = "Informe o porte do animal.")
        @Size(max = 30, message = "O porte deve ter no maximo 30 caracteres.")
        String porteAnimal) {
}
