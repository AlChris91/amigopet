package com.amigopet.api.util;

/**
 * Calculo da distancia em quilometros entre duas coordenadas geograficas.
 *
 * <p>Utiliza a formula de Haversine, que mede a distancia sobre a superficie
 * da Terra (modelo esfera) e e suficiente para o raio de busca de ate 20 km
 * exigido pelo projeto.</p>
 */
public final class CalculadoraDistancia {

    /** Raio medio da Terra em quilometros. */
    private static final double RAIO_TERRA_KM = 6371.0088;

    private CalculadoraDistancia() {
    }

    /**
     * Calcula a distancia em linha reta (superficie) entre dois pontos.
     *
     * @param latitude1  latitude do primeiro ponto em graus
     * @param longitude1 longitude do primeiro ponto em graus
     * @param latitude2  latitude do segundo ponto em graus
     * @param longitude2 longitude do segundo ponto em graus
     * @return distancia em quilometros, ou {@code Double.NaN} se alguma coordenada for nula
     */
    public static double distanciaKm(Double latitude1, Double longitude1, Double latitude2, Double longitude2) {
        if (latitude1 == null || longitude1 == null || latitude2 == null || longitude2 == null) {
            return Double.NaN;
        }

        double latitudeRad1 = Math.toRadians(latitude1);
        double latitudeRad2 = Math.toRadians(latitude2);
        double deltaLatitude = Math.toRadians(latitude2 - latitude1);
        double deltaLongitude = Math.toRadians(longitude2 - longitude1);

        double senoLatMeio = Math.sin(deltaLatitude / 2);
        double senoLonMeio = Math.sin(deltaLongitude / 2);

        double a = senoLatMeio * senoLatMeio
                + Math.cos(latitudeRad1) * Math.cos(latitudeRad2) * senoLonMeio * senoLonMeio;

        return RAIO_TERRA_KM * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    /**
     * Calcula o ponto medio (centroide) de um conjunto de coordenadas.
     *
     * @param latitudes  latitudes do conjunto
     * @param longitudes longitudes do conjunto, na mesma ordem das latitudes
     * @return array com latitude e longitude do centroide, ou {@code null} se o conjunto for vazio
     */
    public static double[] centroide(java.util.List<Double> latitudes, java.util.List<Double> longitudes) {
        if (latitudes == null || longitudes == null || latitudes.isEmpty()) {
            return null;
        }

        double somaLatitude = 0;
        double somaLongitude = 0;
        int validos = 0;

        for (int i = 0; i < latitudes.size(); i++) {
            Double latitude = latitudes.get(i);
            Double longitude = longitudes.get(i);
            if (latitude != null && longitude != null) {
                somaLatitude += latitude;
                somaLongitude += longitude;
                validos++;
            }
        }

        if (validos == 0) {
            return null;
        }
        return new double[] { somaLatitude / validos, somaLongitude / validos };
    }
}
