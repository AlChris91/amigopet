package com.amigopet.api.model;

/**
 * Situacoes possiveis de um atendimento na fila virtual.
 *
 * <p>O enum substitui o uso de {@code String} livre no atributo
 * {@link Atendimento#statusAtendimento}, garantindo que apenas valores
 * validos sejam persistidos na tabela {@code atendimentos}.</p>
 */
public enum StatusAtendimento {

    /** O pet foi registrado e esta aguardando a ordem de chegada na fila. */
    BUSCANDO,

    /** A clinica chamou o tutor para o consultorio. */
    CHAMADO,

    /** O atendimento esta em andamento. */
    ATENDIDO,

    /** O atendimento foi cancelado pelo tutor ou pela clinica. */
    CANCELADO,

    /** O atendimento foi finalizado e o pet liberado. */
    FINALIZADO;

    /**
     * Verifica se o texto informado corresponde a um status valido.
     *
     * @param status texto recebido do front-end
     * @return {@code true} quando o status existe e nao esta em branco
     */
    public static boolean isValido(String status) {
        if (status == null || status.isBlank()) {
            return false;
        }
        for (StatusAtendimento valor : values()) {
            if (valor.name().equalsIgnoreCase(status.trim())) {
                return true;
            }
        }
        return false;
    }
}
