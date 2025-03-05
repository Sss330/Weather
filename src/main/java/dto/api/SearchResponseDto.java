package dto.api;


import lombok.Data;

import java.util.List;

@Data
public class SearchResponseDto {
    private List<WeatherResponseDto> list;
}