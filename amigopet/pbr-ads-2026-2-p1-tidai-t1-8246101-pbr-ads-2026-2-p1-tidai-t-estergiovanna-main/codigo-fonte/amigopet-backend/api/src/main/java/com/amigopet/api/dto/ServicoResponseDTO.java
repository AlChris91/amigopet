package com.amigopet.api.dto;

import java.math.BigDecimal;

import com.amigopet.api.model.Servico;

/**
 * Servico oferecido por uma clinica parceira.
 *
 * @param id                       identificador do servico
 * @param clinicaId                clinica que oferece o servico
 * @param nomeServico              nome do servico
 * @param preco                    preco praticado
 * @param especieAtendida          especies atendidas no servico
 * @param orientacaoPreAtendimento orientacoes pré-atendimento para o tutor
 */
public record ServicoResponseDTO(
        Long id, 
        Long clinicaId, 
        String nomeServico, 
        BigDecimal preco,
        String especieAtendida,
        String orientacaoPreAtendimento) {

    public static ServicoResponseDTO from(Servico servico) {
        return new ServicoResponseDTO(
                servico.getId(),
                servico.getClinicaId(),
                servico.getNomeServico(),
                servico.getPreco(),
                servico.getEspecieAtendida(),
                servico.getOrientacaoPreAtendimento());
    }
}
