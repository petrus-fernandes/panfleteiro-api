package br.com.promo.panfleteiro.util;



import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BoundingBoxCalculator {
    private static final double EARTH_RADIUS_KM = 6371.0;

    public static Map<String, Double> calculateBoundingBox(double lat, double lon, double rangeInKm) {
        double latRad = Math.toRadians(lat);

        double deltaLat = rangeInKm / EARTH_RADIUS_KM;
        double deltaLon = Math.asin(Math.sin(deltaLat) / Math.cos(latRad));

        double minLat = lat - Math.toDegrees(deltaLat);
        double maxLat = lat + Math.toDegrees(deltaLat);
        double minLon = lon - Math.toDegrees(deltaLon);
        double maxLon = lon + Math.toDegrees(deltaLon);

        Map<String, Double> boundingBox = new HashMap<>();
        boundingBox.put("minLat", minLat);
        boundingBox.put("maxLat", maxLat);
        boundingBox.put("minLon", minLon);
        boundingBox.put("maxLon", maxLon);

        return boundingBox;
    }
}