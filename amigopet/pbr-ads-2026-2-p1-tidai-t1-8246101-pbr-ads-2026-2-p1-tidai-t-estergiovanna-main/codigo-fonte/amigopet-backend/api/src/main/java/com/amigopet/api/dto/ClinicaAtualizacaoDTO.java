package com.amigopet.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Dados enviados pelo painel administrativo na edicao do perfil da clinica.
 *
 * @param nomeFantasia    novo nome exibido ao tutor
 * @param emailAdm        novo e-mail administrativo para login
 * @param cep             novo CEP comercial
 * @param endereco        novo endereco completo
 * @param telefone        novo telefone de contato
 * @param senhaAdm        nova senha, opcional: quando vazia a senha atual e mantida
 * @param especieAtendida novas especies de animais atendidos pela clinica
 */
public record ClinicaAtualizacaoDTO(
        @NotBlank(message = "O nome fantasia da clinica e obrigatorio.")
        @Size(max = 100, message = "O nome fantasia deve ter no maximo 100 caracteres.")
        String nomeFantasia,

        @NotBlank(message = "O e-mail da clinica e obrigatorio.")
        @Email(message = "Informe um e-mail valido.")
        @Size(max = 100, message = "O e-mail deve ter no maximo 100 caracteres.")
        String emailAdm,

        @NotBlank(message = "O CEP da clinica e obrigatorio.")
        @Pattern(regexp = "\\d{5}-?\\d{3}", message = "Informe um CEP valido, ex.: 31270-010.")
        String cep,

        @NotBlank(message = "O endereco da clinica e obrigatorio.")
        @Size(max = 255, message = "O endereco deve ter no maximo 255 caracteres.")
        String endereco,

        @NotBlank(message = "O telefone da clinica e obrigatorio.")
        @Size(max = 20, message = "O telefone deve ter no maximo 20 caracteres.")
        String telefone,

        @Size(max = 60, message = "A senha deve ter no maximo 60 caracteres.")
        String senhaAdm,

        @Size(max = 50, message = "O campo de especies deve ter no maximo 50 caracteres.")
        String especieAtendida) {
}
