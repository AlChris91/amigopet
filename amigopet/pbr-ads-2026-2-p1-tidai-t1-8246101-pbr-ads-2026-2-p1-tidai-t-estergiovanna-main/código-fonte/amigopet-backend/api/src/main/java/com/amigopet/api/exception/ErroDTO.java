package com.amigopet.api.exception;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Corpo padronizado das respostas de erro da API.
 *
 * <p>O front-end le o campo {@code mensagem} para exibir o aviso ao usuario,
 * por isso ele e sempre preenchido.</p>
 *
 * @param timestamp momento em que o erro ocorreu
 * @param status    codigo HTTP devolvido
 * @param mensagem  descricao do erro
 * @param erros     detalhes campo a campo, presente apenas em erros de validacao
 */
public record ErroDTO(
        LocalDateTime timestamp,
        int status,
        String mensagem,
        Map<String, String> erros) {

    public static ErroDTO de(int status, String mensagem) {
        return new ErroDTO(LocalDateTime.now(), status, mensagem, null);
    }

    public static ErroDTO de(int status, String mensagem, Map<String, String> erros) {
        return new ErroDTO(LocalDateTime.now(), status, mensagem, erros);
    }
}
