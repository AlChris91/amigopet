package com.amigopet.api.dto;

import com.amigopet.api.model.Clinica;

/**
 * Representacao publica de uma clinica parceira.
 *
 * <p>Nao expoe o campo {@code senhaAdm}, evitando que o hash trafegue na
 * resposta da API.</p>
 *
 * @param id              identificador da clinica
 * @param nomeFantasia    nome exibido ao tutor
 * @param emailAdm        e-mail administrativo
 * @param cep             CEP comercial
 * @param endereco        endereco completo
 * @param telefone        telefone de contato
 * @param latitude        latitude da clinica
 * @param longitude       longitude da clinica
 * @param especieAtendida especies de animais atendidos pela clinica
 */
public record ClinicaResponseDTO(
        Long id,
        String nomeFantasia,
        String emailAdm,
        String cep,
        String endereco,
        String telefone,
        Double latitude,
        Double longitude,
        String especieAtendida) {

    public static ClinicaResponseDTO from(Clinica clinica) {
        return new ClinicaResponseDTO(
                clinica.getId(),
                clinica.getNomeFantasia(),
                clinica.getEmailAdm(),
                clinica.getCep(),
                clinica.getEndereco(),
                clinica.getTelefone(),
                clinica.getLatitude(),
                clinica.getLongitude(),
                clinica.getEspecieAtendida());
    }
}
