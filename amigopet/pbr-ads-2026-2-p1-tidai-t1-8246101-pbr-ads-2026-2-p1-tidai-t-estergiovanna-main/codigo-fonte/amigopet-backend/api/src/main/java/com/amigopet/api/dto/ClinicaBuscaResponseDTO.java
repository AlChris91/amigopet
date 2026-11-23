package com.amigopet.api.dto;

import com.amigopet.api.model.Clinica;

/**
 * Clinica retornada pela busca de prestadores.
 *
 * <p>O campo {@code senhaAdm} nunca e exposto. A {@code distancia} e calculada
 * pelo back-end em quilometros e chega pronta para o front-end.</p>
 *
 * @param id          identificador da clinica
 * @param nomeFantasia nome exibido ao tutor
 * @param cep         CEP comercial
 * @param endereco    endereco completo
 * @param telefone    telefone de contato
 * @param latitude    latitude da clinica
 * @param longitude   longitude da clinica
 * @param distancia   distancia em quilometros a partir da referencia de busca
 */
public record ClinicaBuscaResponseDTO(
        Long id,
        String nomeFantasia,
        String cep,
        String endereco,
        String telefone,
        Double latitude,
        Double longitude,
        Double distancia) {

    public static ClinicaBuscaResponseDTO from(Clinica clinica, double distanciaKm) {
        return new ClinicaBuscaResponseDTO(
                clinica.getId(),
                clinica.getNomeFantasia(),
                clinica.getCep(),
                clinica.getEndereco(),
                clinica.getTelefone(),
                clinica.getLatitude(),
                clinica.getLongitude(),
                Math.round(distanciaKm * 10.0) / 10.0);
    }
}
