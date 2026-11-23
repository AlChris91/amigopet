package com.amigopet.api.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Tratamento centralizado de erros da API.
 *
 * <p>Converte as excecoes do Spring e do Hibernate em respostas JSON com o
 * campo {@code mensagem}, formato ja consumido pelo front-end, evitando que o
 * usuario veja paginas de erro do servidor.</p>
 */
@RestControllerAdvice
public class ManipuladorGlobalDeExcecoes {

    private static final Logger log = LoggerFactory.getLogger(ManipuladorGlobalDeExcecoes.class);

    /** Erros de regra de negocio lancados pela camada de servico. */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErroDTO> handleApiException(ApiException excecao) {
        return ResponseEntity.status(excecao.getStatus())
                .body(ErroDTO.de(excecao.getStatus().value(), excecao.getMessage()));
    }

    /** Falhas de validacao dos campos recebidos no corpo da requisicao. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroDTO> handleValidacao(MethodArgumentNotValidException excecao) {
        Map<String, String> erros = new LinkedHashMap<>();
        for (FieldError erro : excecao.getBindingResult().getFieldErrors()) {
            erros.put(erro.getField(), erro.getDefaultMessage());
        }
        String mensagem = erros.isEmpty()
                ? "Dados invalidos na requisicao."
                : erros.values().iterator().next();
        return ResponseEntity.badRequest().body(ErroDTO.de(HttpStatus.BAD_REQUEST.value(), mensagem, erros));
    }

    /** Consultas com parametro invalido, como um id que nao existe no caminho. */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroDTO> handleTipoInvalido(MethodArgumentTypeMismatchException excecao) {
        return ResponseEntity.badRequest()
                .body(ErroDTO.de(HttpStatus.BAD_REQUEST.value(),
                        "Valor invalido para o parametro '" + excecao.getName() + "'."));
    }

    /** Corpo da requisicao com JSON malformado ou com campos de tipo errado. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroDTO> handleCorpoInvalido(HttpMessageNotReadableException excecao) {
        return ResponseEntity.badRequest()
                .body(ErroDTO.de(HttpStatus.BAD_REQUEST.value(), "Corpo da requisicao invalido ou ausente."));
    }

    /** Violacao de chave estrangeira ou de unicidade mapeada pelo Hibernate. */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroDTO> handleIntegridade(DataIntegrityViolationException excecao) {
        log.warn("Violacao de integridade no banco de dados: {}", excecao.getMostSpecificCause().getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErroDTO.de(HttpStatus.CONFLICT.value(),
                        "Ja existe um registro com estes dados ou algum vinculo informado e invalido."));
    }

    /** Ultima instancia: qualquer falha inesperada vira 500 sem expor detalhes internos. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroDTO> handleGenerico(Exception excecao) {
        log.error("Erro inesperado na API", excecao);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErroDTO.de(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Erro interno no servidor. Tente novamente mais tarde."));
    }
}
