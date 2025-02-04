package dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenWeatherResponseDto {

    private String name;
    BigDecimal latitude;
    BigDecimal longitude;

}
