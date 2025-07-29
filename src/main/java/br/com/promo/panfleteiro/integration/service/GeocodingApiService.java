package br.com.promo.panfleteiro.integration.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;

import br.com.promo.panfleteiro.entity.Location;

@Service
public class GeocodingApiService {

    private final String apiKey;

    public GeocodingApiService(@Value("${google.api.key}") String apiKey) {
        this.apiKey = apiKey;
    }

    public Location getLocationWithAddress(String address) {
        GeocodingResult[] response = getGeocodingResult(address);
        return toLocation(response[0]);
    }

    public GeocodingResult[] getGeocodingResult (String address) {
        GeoApiContext context = new GeoApiContext.Builder().apiKey(apiKey).build();
        GeocodingResult[] response;
        try {
            response = GeocodingApi.geocode(context, address).await();
        } catch (ApiException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
        context.shutdown();
        return response;
    }

    private Location toLocation(GeocodingResult result) {
        Location location = new Location();
        location.setLatitude(result.geometry.location.lat);
        location.setLongitude(result.geometry.location.lng);
        location.setAddress(result.formattedAddress);
        return location;
    }
}
