package com.amigopet.api.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Credenciais enviadas no login do administrador da clinica.
 *
 * @param emailAdm e-mail administrativo cadastrado
 * @param senhaAdm senha administrativa cadastrada
 */
public record ClinicaLoginDTO(
        @NotBlank(message = "O e-mail do administrador e obrigatorio.")
        String emailAdm,

        @NotBlank(message = "A senha do administrador e obrigatoria.")
        String senhaAdm) {
}
