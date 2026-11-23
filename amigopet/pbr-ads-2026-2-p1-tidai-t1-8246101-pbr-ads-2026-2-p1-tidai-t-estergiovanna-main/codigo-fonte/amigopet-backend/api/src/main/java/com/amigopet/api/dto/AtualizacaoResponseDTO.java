package com.amigopet.api.dto;

/**
 * Resposta padronizada das operacoes de atualizacao de perfil.
 *
 * @param nome     nome ja salvo no banco
 * @param mensagem aviso exibido ao usuario
 */
public record AtualizacaoResponseDTO(String nome, String mensagem) {
}
