package com.amigopet.api.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NormalizadorTextoTest {

    @Test
    @DisplayName("Remove acentos e reduz para minusculas")
    void removeAcentos() {
        assertThat(NormalizadorTexto.semAcento("Vacinação Antirrábica")).isEqualTo("vacinacao antirrabica");
        assertThat(NormalizadorTexto.semAcento("Castração")).isEqualTo("castracao");
        assertThat(NormalizadorTexto.semAcento("Consulta Geral")).isEqualTo("consulta geral");
    }

    @Test
    @DisplayName("Texto nulo devolve string vazia")
    void trataTextoNulo() {
        assertThat(NormalizadorTexto.semAcento(null)).isEmpty();
    }

    @Test
    @DisplayName("Extrai somente os digitos do CEP")
    void extraiDigitosDoCep() {
        assertThat(NormalizadorTexto.somenteDigitos("31270-010")).isEqualTo("31270010");
        assertThat(NormalizadorTexto.somenteDigitos("31270010")).isEqualTo("31270010");
        assertThat(NormalizadorTexto.somenteDigitos(null)).isEmpty();
    }

    @Test
    @DisplayName("Identifica a regiao a partir dos cinco primeiros digitos do CEP")
    void identificaRegiao() {
        assertThat(NormalizadorTexto.regiaoDoCep("31270-010")).isEqualTo("31270");
        assertThat(NormalizadorTexto.regiaoDoCep("30190-060")).isEqualTo("30190");
        assertThat(NormalizadorTexto.regiaoDoCep("123")).isEqualTo("123");
    }

    @Test
    @DisplayName("Compara CEPs da mesma regiao")
    void comparaRegioes() {
        assertThat(NormalizadorTexto.mesmaRegiao("31270-010", "31270")).isTrue();
        assertThat(NormalizadorTexto.mesmaRegiao("31270-901", "31270")).isTrue();
        assertThat(NormalizadorTexto.mesmaRegiao("30190-060", "31270")).isFalse();
    }

    @Test
    @DisplayName("Sem regiao de referencia todas as clinicas sao consideradas")
    void semRegiaoAceitaTudo() {
        assertThat(NormalizadorTexto.mesmaRegiao("30190-060", "")).isTrue();
        assertThat(NormalizadorTexto.mesmaRegiao("30190-060", null)).isTrue();
    }
}
