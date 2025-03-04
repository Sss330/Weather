package service;

import dto.api.SearchQuery;
import dto.api.WeatherResponseDto;
import exception.DeletingLocationException;
import exception.LocationNotFoundException;
import exception.SavingLocationException;
import exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Location;
import model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.LocationRepository;
import repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LocationService {

    private final LocationRepository locationRepository;
    private final OpenWeatherApiService openWeatherService;
    private final UserRepository userRepository;


    public List<WeatherResponseDto> getLocationByArea(SearchQuery searchQuery) {
        try {
            return openWeatherService.getLocationByArea(searchQuery);
        } catch (Exception e) {
            throw new LocationNotFoundException("Can`t find location by area" + e);
        }
    }

    public WeatherResponseDto getLocationByCoordinates(BigDecimal lat, BigDecimal lon) {
        try {

            return openWeatherService.getLocationByCoordinates(lat, lon);
        } catch (Exception e) {
            throw new LocationNotFoundException("Can`t find location by coordinates" + e);
        }
    }

    @Transactional(readOnly = true)
    public List<Location> getUsersLocations(User user) {
        userRepository.getUserByLogin(user.getLogin())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return locationRepository.findLocationsByUserId(user);
    }

    public void saveLocation(Location location) {
        try {
            locationRepository.save(location);
        } catch (Exception e) {
            throw new SavingLocationException("Can`t save location" + e);
        }
    }


    public void deleteLocationById(User user, Long locationId) {
        try {
            locationRepository.deleteByCoordinates(user, locationId);
        } catch (Exception e) {
            throw new DeletingLocationException("Can`t delete location by user id" + e);
        }
    }
}
