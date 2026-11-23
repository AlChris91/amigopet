package com.amigopet.api.dto;

/**
 * Resposta padronizada das operacoes de login.
 *
 * @param id       identificador do usuario autenticado
 * @param nome     nome exibido no front-end
 * @param mensagem aviso exibido ao usuario
 */
public record LoginResponseDTO(Long id, String nome, String mensagem) {
}
