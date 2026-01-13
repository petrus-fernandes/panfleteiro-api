package br.com.promo.panfleteiro.util;

public final class HaversineCalculator {

    private static final double EARTH_RADIUS_KM = 6371.0;

    private HaversineCalculator() {
    }

    public static double distanceInKm(
            double originLatitude,
            double originLongitude,
            double destinationLatitude,
            double destinationLongitude
    ) {
        double latitudeDifferenceInRadians = Math.toRadians(destinationLatitude - originLatitude);

        double longitudeDifferenceInRadians = Math.toRadians(destinationLongitude - originLongitude);

        double originLatitudeInRadians = Math.toRadians(originLatitude);

        double destinationLatitudeInRadians = Math.toRadians(destinationLatitude);

        double haversineFormula = Math.pow(Math.sin(latitudeDifferenceInRadians / 2), 2)
                + Math.cos(originLatitudeInRadians)
                * Math.cos(destinationLatitudeInRadians)
                * Math.pow(Math.sin(longitudeDifferenceInRadians / 2), 2);

        double centralAngle = 2 * Math.atan2(
                Math.sqrt(haversineFormula),
                Math.sqrt(1 - haversineFormula)
        );

        return EARTH_RADIUS_KM * centralAngle;
    }
}

