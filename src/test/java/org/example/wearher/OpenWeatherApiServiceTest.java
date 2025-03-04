package org.example.wearher;

import dto.api.SearchQuery;
import dto.api.SearchResponseDto;
import dto.api.WeatherResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import service.OpenWeatherApiService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OpenWeatherApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OpenWeatherApiService weatherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetLocationByCoordinates() {
        BigDecimal lat = new BigDecimal("40.7128");
        BigDecimal lon = new BigDecimal("-74.0060");
        String expectedUrl = "https://api.openweathermap.org/data/2.5/weather?lat=40.7128&lon=-74.0060&lang=ru&appid=d59c1c23128a9162cc2fffe5623b6f5d&units=metric";

        WeatherResponseDto mockResponse = new WeatherResponseDto();
        when(restTemplate.getForObject(eq(expectedUrl), eq(WeatherResponseDto.class)))
                .thenReturn(mockResponse);

        WeatherResponseDto result = weatherService.getLocationByCoordinates(lat, lon);
        assertEquals(mockResponse, result);
    }

    @Test
    void testGetLocationByArea() {
        SearchQuery query = new SearchQuery();
        query.setArea("New York");

        WeatherResponseDto mockWeather = new WeatherResponseDto();
        SearchResponseDto mockSearchResponse = new SearchResponseDto();
        mockSearchResponse.setList(List.of(mockWeather));

        when(restTemplate.getForObject(anyString(), eq(SearchResponseDto.class)))
                .thenReturn(mockSearchResponse);

        List<WeatherResponseDto> result = weatherService.getLocationByArea(query);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }


}
