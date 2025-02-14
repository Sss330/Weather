package mapper;

import dto.api.LocationResponseDto;
import model.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LocationMapper {
    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    @Mapping(target = "id", ignore = true)

    Location toEntity(LocationResponseDto locationResponseDto);

    LocationResponseDto toDto(Location location);
}
