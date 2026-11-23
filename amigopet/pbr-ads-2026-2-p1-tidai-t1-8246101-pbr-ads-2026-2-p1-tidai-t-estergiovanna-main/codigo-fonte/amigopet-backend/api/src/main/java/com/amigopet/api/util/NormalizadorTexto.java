package com.amigopet.api.util;

import java.text.Normalizer;
import java.util.Locale;

/**
 * Utilitarios de normalizacao de texto usados nos filtros da busca.
 *
 * <p>O banco de dados grava os nomes dos servicos com acentos ("Castração" com
 * acento, "Consulta Geral", "Exames de Sangue"), enquanto o front-end envia
 * categorias simplificadas como "Castracao" ou "Vacinacao" tambem sem acento.
 * A comparacao direta falharia, por isso ambos os lados sao normalizados
 * antes de serem confrontados.</p>
 */
public final class NormalizadorTexto {

    private NormalizadorTexto() {
    }

    /**
     * Remove acentos, converte para minusculas e reduz espacos duplicados.
     *
     * @param texto texto de origem, pode ser {@code null}
     * @return texto normalizado ou string vazia quando a entrada for nula
     */
    public static String semAcento(String texto) {
        if (texto == null) {
            return "";
        }
        String semAcento = Normalizer.normalize(texto, Normalizer.Form.NFD);
        semAcento = semAcento.replaceAll("\\p{M}", "");
        return semAcento.toLowerCase(Locale.ROOT).trim();
    }

    /**
     * Extrai apenas os digitos de um texto, usado para normalizar CEP.
     *
     * @param texto texto de origem, pode ser {@code null}
     * @return sequencia de digitos ou string vazia
     */
    public static String somenteDigitos(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replaceAll("\\D", "");
    }

    /**
     * Retorna os cinco primeiros digitos do CEP, que em Belo Horizonte
     * identificam a regiao (Pampulha, Santo Agostinho, Prado, Ouro Preto...).
     *
     * @param cep CEP do usuario
     * @return prefixo regional do CEP ou string vazia
     */
    public static String regiaoDoCep(String cep) {
        String digitos = somenteDigitos(cep);
        return digitos.length() >= 5 ? digitos.substring(0, 5) : digitos;
    }

    /**
     * Verifica se o CEP pertence a mesma regiao do CEP de referencia.
     *
     * @param cep          CEP avaliado
     * @param regiaoReferencia prefixo regional usado como referencia
     * @return {@code true} quando os dois CEPs compartilham o prefixo
     */
    public static boolean mesmaRegiao(String cep, String regiaoReferencia) {
        if (regiaoReferencia == null || regiaoReferencia.isBlank()) {
            return true;
        }
        String digitos = somenteDigitos(cep);
        return digitos.startsWith(regiaoReferencia);
    }
}
