package com.amigopet.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Dados enviados pelo front-end na edicao do perfil do tutor.
 *
 * @param nome  novo nome do tutor
 * @param cep   novo CEP residencial
 * @param senha nova senha, opcional: quando vazia a senha atual e mantida
 */
public record TutorAtualizacaoDTO(
        @NotBlank(message = "O nome do tutor e obrigatorio.")
        @Size(max = 100, message = "O nome deve ter no maximo 100 caracteres.")
        String nome,

        @NotBlank(message = "O CEP do tutor e obrigatorio.")
        @Pattern(regexp = "\\d{5}-?\\d{3}", message = "Informe um CEP valido, ex.: 30123-450.")
        String cep,

        @Size(max = 60, message = "A senha deve ter no maximo 60 caracteres.")
        String senha) {
}
