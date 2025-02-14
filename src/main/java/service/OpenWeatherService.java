package service;

import dto.api.LocationResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenWeatherService {

    private final RestTemplate restTemplate;

    @Value("${openweather.api.key}")
    private String apiKey;

    @Value("${openweather.api.url}")
    private String apiUrl;


    public List<LocationResponseDto>  getLocationByCoordinates(BigDecimal lat, BigDecimal lon){
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .path("/data/2.5/weather")
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .toUriString();
        log.info("Requesting Open Weather api by {}", url);
        return Collections.singletonList(restTemplate.getForObject(url, LocationResponseDto.class));
    }

    public LocationResponseDto getLocationByArea (String area){
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .path("/data/2.5/weather")
                .queryParam("q", area)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .toUriString();

        log.info("Requesting weather data by {}", url);
        return restTemplate.getForObject(url, LocationResponseDto.class);

    }
}
