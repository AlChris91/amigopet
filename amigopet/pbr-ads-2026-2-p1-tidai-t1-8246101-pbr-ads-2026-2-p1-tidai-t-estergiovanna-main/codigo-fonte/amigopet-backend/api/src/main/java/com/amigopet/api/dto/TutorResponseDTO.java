package com.amigopet.api.dto;

import com.amigopet.api.model.Tutor;

/**
 * Representacao publica de um tutor.
 *
 * <p>Nao expoe o campo {@code senha}, evitando que o hash trafegue na resposta
 * da API.</p>
 *
 * @param id    identificador do tutor
 * @param nome  nome do tutor
 * @param email e-mail do tutor
 * @param cep   CEP residencial
 */
public record TutorResponseDTO(Long id, String nome, String email, String cep) {

    public static TutorResponseDTO from(Tutor tutor) {
        return new TutorResponseDTO(tutor.getId(), tutor.getNome(), tutor.getEmail(), tutor.getCep());
    }
}
