package dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponseDto {

    @JsonProperty("coord")
    private Coordinates coordinates;

    @JsonProperty("weather")
    private List<WeatherInfo> weather;

    @JsonProperty("main")
    private MainData main;

    @JsonProperty("visibility")
    private Integer visibility;

    @JsonProperty("wind")
    private Wind wind;

    @JsonProperty("clouds")
    private Clouds clouds;

    @JsonProperty("dt")
    private Long timestamp;

    @JsonProperty("sys")
    private SysInfo sys;

    @JsonProperty("timezone")
    private Integer timezoneShift;

    @JsonProperty("id")
    private Long cityId;

    @JsonProperty("name")
    private String name;

    // included classes
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Coordinates {
        @JsonProperty("lon")
        private BigDecimal lon;
        @JsonProperty("lat")
        private BigDecimal lat;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WeatherInfo {
        @JsonProperty("id")
        private Integer id;
        @JsonProperty("main")
        private String group;
        @JsonProperty("description")
        private String description;
        @JsonProperty("icon")
        private String iconCode;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MainData {
        
        @JsonProperty("temp")
        private BigDecimal temperature;

        @JsonProperty("feels_like")
        private BigDecimal feelsLike;

        @JsonProperty("temp_min")
        private BigDecimal tempMin;

        @JsonProperty("temp_max")
        private BigDecimal tempMax;

        @JsonProperty("pressure")
        private Integer pressure;

        @JsonProperty("humidity")
        private Integer humidity;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Wind {
        @JsonProperty("speed")
        private BigDecimal speed;
        @JsonProperty("deg")
        private Integer direction;
        @JsonProperty("gust")
        private BigDecimal gustSpeed;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Clouds {
        @JsonProperty("all")
        private Integer cloudiness;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SysInfo {
        @JsonProperty("country")
        private String country;
        @JsonProperty("state")
        private String state;
        @JsonProperty("sunrise")
        private Long sunriseTime;
        @JsonProperty("sunset")
        private Long sunsetTime;
    }
}