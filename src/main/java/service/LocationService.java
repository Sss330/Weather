package service;

import dto.api.LocationResponseDto;
import exception.DeletingLocationException;
import exception.SavingLocationException;
import lombok.RequiredArgsConstructor;
import mapper.LocationMapper;
import model.Location;
import org.springframework.stereotype.Service;
import repository.LocationRepository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final OpenWeatherService openWeatherService;

    public Location getLocationByArea(String area) {
        return LocationMapper.INSTANCE.toEntity(openWeatherService.getLocationByArea(area));
    }

    public List<LocationResponseDto> getLocationByCoordinates(BigDecimal lat, BigDecimal lon) {

        return LocationMapper.INSTANCE.toEntity(openWeatherService.getLocationByCoordinates(lat, lon));
    }

    public void saveLocation(Location location) {
        try {
            locationRepository.save(location);
        } catch (Exception e) {
            throw new SavingLocationException("Can`t save location" + e);
        }
    }
    public void deleteLocation(Long id) {
        try {
            locationRepository.deleteById(id);
        } catch (Exception e) {
            throw new SavingLocationException("Can`t delete location" + e);
        }
    }
    public List<Location> getAllLocation(Long id) {
        try {
           return locationRepository.findAll().orElse(Collections.emptyList());
        } catch (Exception e) {
            throw new SavingLocationException("Can`t get all locations " + e);
        }
    }

    public void deleteLocation(LocationResponseDto locationDto) {
        try {
            Location location = LocationMapper.INSTANCE.toEntity(locationDto);
            locationRepository.delete(location);
        } catch (Exception e) {
            throw new DeletingLocationException("Can`t delete location" + e);
        }
    }
}