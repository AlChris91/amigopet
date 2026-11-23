package com.amigopet.api.exception;

import org.springframework.http.HttpStatus;

/**
 * Erro de regra de negocio, tratado pelo {@link ManipuladorGlobalDeExcecoes}.
 *
 * <p>Permite que a camada de servico interrompa o fluxo com uma mensagem
 * clara para o usuario e o codigo HTTP correspondente, em vez de deixar a
 * excecao chegar ao banco de dados e virar um erro 500 generico.</p>
 */
public class ApiException extends RuntimeException {

    private final HttpStatus status;

    public ApiException(HttpStatus status, String mensagem) {
        super(mensagem);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    /** Atalho para erros de cliente (400 - requisicao invalida). */
    public static ApiException badRequest(String mensagem) {
        return new ApiException(HttpStatus.BAD_REQUEST, mensagem);
    }

    /** Atalho para recurso inexistente (404). */
    public static ApiException notFound(String mensagem) {
        return new ApiException(HttpStatus.NOT_FOUND, mensagem);
    }

    /** Atalho para conflito de dados (409 - e-mail ja cadastrado). */
    public static ApiException conflict(String mensagem) {
        return new ApiException(HttpStatus.CONFLICT, mensagem);
    }

    /** Atalho para falha de autenticacao (401). */
    public static ApiException unauthorized(String mensagem) {
        return new ApiException(HttpStatus.UNAUTHORIZED, mensagem);
    }
}
