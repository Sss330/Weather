package dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationNamesResponseDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("local_names")
    private Map<String, String> localNames;

    @JsonProperty("lat")
    private BigDecimal lat;

    @JsonProperty("lon")
    private BigDecimal lon;

    @JsonProperty("country")
    private String country;

    @JsonProperty("state")
    private String state;
}