package com.amigopet.api.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CalculadoraDistanciaTest {

    @Test
    @DisplayName("Um grau de longitude no equador vale cerca de 111 km")
    void distanciaNoEquador() {
        double distancia = CalculadoraDistancia.distanciaKm(0.0, 0.0, 0.0, 1.0);

        assertThat(distancia).isCloseTo(111.19, within(0.5));
    }

    @Test
    @DisplayName("Um grau de latitude vale cerca de 111,19 km")
    void distanciaEmLatitude() {
        double distancia = CalculadoraDistancia.distanciaKm(0.0, 0.0, 1.0, 0.0);

        assertThat(distancia).isCloseTo(111.19, within(0.5));
    }

    @Test
    @DisplayName("Um grau de longitude encurta conforme a latitude aumenta")
    void distanciaEmLongitudeSobeComALatitude() {
        double noEquador = CalculadoraDistancia.distanciaKm(0.0, 0.0, 0.0, 1.0);
        double emSessentaGraus = CalculadoraDistancia.distanciaKm(60.0, 0.0, 60.0, 1.0);

        assertThat(noEquador).isCloseTo(111.19, within(0.5));
        assertThat(emSessentaGraus).isCloseTo(55.60, within(0.5));
    }

    @Test
    @DisplayName("Distancia entre dois pontos identicos e zero")
    void distanciaZero() {
        double distancia = CalculadoraDistancia.distanciaKm(-19.9247, -43.9351, -19.9247, -43.9351);

        assertThat(distancia).isCloseTo(0.0, within(0.0001));
    }

    @Test
    @DisplayName("Coordenada nula devolve NaN em vez de quebrar o filtro")
    void coordenadaNula() {
        double distancia = CalculadoraDistancia.distanciaKm(null, -43.9351, -19.9247, -43.9351);

        assertThat(distancia).isNaN();
    }

    @Test
    @DisplayName("Centroide e a media das coordenadas informadas")
    void calculaCentroide() {
        double[] centroide = CalculadoraDistancia.centroide(
                List.of(-19.869123, -19.863145),
                List.of(-43.963456, -43.971254));

        assertThat(centroide).isNotNull();
        assertThat(centroide[0]).isCloseTo(-19.866134, within(0.000001));
        assertThat(centroide[1]).isCloseTo(-43.967355, within(0.000001));
    }

    @Test
    @DisplayName("Centroide de lista vazia devolve nulo")
    void centroideVazio() {
        assertThat(CalculadoraDistancia.centroide(List.of(), List.of())).isNull();
    }
}
