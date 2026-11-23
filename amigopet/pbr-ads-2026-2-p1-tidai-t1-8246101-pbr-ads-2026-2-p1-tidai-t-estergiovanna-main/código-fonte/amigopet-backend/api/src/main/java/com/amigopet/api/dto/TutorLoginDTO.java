package com.amigopet.api.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Credenciais enviadas no login do tutor.
 *
 * @param email e-mail cadastrado
 * @param senha senha cadastrada
 */
public record TutorLoginDTO(
        @NotBlank(message = "O e-mail e obrigatorio.")
        String email,

        @NotBlank(message = "A senha e obrigatoria.")
        String senha) {
}
