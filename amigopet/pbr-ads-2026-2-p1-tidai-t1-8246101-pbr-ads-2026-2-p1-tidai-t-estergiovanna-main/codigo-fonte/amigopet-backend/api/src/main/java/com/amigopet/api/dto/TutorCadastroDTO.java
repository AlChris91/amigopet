package com.amigopet.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Dados enviados pelo front-end no cadastro de um tutor.
 *
 * @param nome  nome completo do tutor
 * @param email e-mail de acesso, precisa ser unico
 * @param cep   CEP residencial do tutor
 * @param senha senha de acesso, nunca e devolvida nas respostas
 */
public record TutorCadastroDTO(
        @NotBlank(message = "O nome do tutor e obrigatorio.")
        @Size(max = 100, message = "O nome deve ter no maximo 100 caracteres.")
        String nome,

        @NotBlank(message = "O e-mail do tutor e obrigatorio.")
        @Size(max = 100, message = "O e-mail deve ter no maximo 100 caracteres.")
        @jakarta.validation.constraints.Email(message = "Informe um e-mail valido.")
        String email,

        @NotBlank(message = "O CEP do tutor e obrigatorio.")
        @Pattern(regexp = "\\d{5}-?\\d{3}", message = "Informe um CEP valido, ex.: 30123-450.")
        String cep,

        @NotBlank(message = "A senha do tutor e obrigatoria.")
        @Size(min = 6, max = 60, message = "A senha deve ter entre 6 e 60 caracteres.")
        String senha) {
}
