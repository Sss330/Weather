package service;

import dto.api.SearchQuery;
import dto.api.SearchResponseDto;
import dto.api.WeatherResponseDto;
import exception.LocationNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenWeatherApiService {

    private final RestTemplate restTemplate;

    //@Value("${openweather.apiKey}")
    private final String apiKey = "d59c1c23128a9162cc2fffe5623b6f5d";

    //@Value("${openweather.baseUrl}")
    private final String apiUrl = "https://api.openweathermap.org";


    public WeatherResponseDto getLocationByCoordinates(BigDecimal lat, BigDecimal lon) {
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .path("/data/2.5/weather")
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("lang", "ru")
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .toUriString();
        log.info("Requesting Open Weather api by coordinates {}", url);
        return restTemplate.getForObject(url, WeatherResponseDto.class);
    }

    public List<WeatherResponseDto> getLocationByArea(SearchQuery searchQuery) {

        try {
            if (searchQuery == null || searchQuery.getArea().isBlank()) {
                throw new IllegalArgumentException("Area is required");
            }

            String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .path("/data/2.5/find")
                    .queryParam("q", searchQuery.getArea())
                    .queryParam("type", "like")
                    .queryParam("lang", "ru")
                    .queryParam("mode", "json")
                    .queryParam("appid", apiKey)
                    .queryParam("units", "metric")
                    .toUriString();
            log.info("Requesting Open Weather api by area {}", url);
            log.info("OpenWeather API URL: {}", apiUrl);


            SearchResponseDto response = restTemplate.getForObject(url, SearchResponseDto.class);
            if (response == null || response.getList() == null) {
                throw new RuntimeException("Response is empty");
            }
            return response.getList();
        } catch (Exception e) {
            throw new LocationNotFoundException("Location not found by area - " + e);
        }

    }
}
