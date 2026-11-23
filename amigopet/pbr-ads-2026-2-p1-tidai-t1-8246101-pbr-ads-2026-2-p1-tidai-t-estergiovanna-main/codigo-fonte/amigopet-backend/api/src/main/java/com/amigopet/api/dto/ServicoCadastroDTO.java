package com.amigopet.api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Dados enviados pela clinica parceira para cadastrar um servico.
 *
 * @param clinicaId   clinica que oferece o servico
 * @param nomeServico nome do servico
 * @param preco       preco praticado
 */
public record ServicoCadastroDTO(
        @NotNull(message = "A clinica do servico e obrigatoria.")
        Long clinicaId,

        @NotBlank(message = "O nome do servico e obrigatorio.")
        @Size(max = 50, message = "O nome do servico deve ter no maximo 50 caracteres.")
        String nomeServico,

        @NotNull(message = "O preco do servico e obrigatorio.")
        @DecimalMin(value = "0.00", message = "O preco nao pode ser negativo.")
        BigDecimal preco,
        String especieAtendida,
        String orientacaoPreAtendimento
        ) {
}
