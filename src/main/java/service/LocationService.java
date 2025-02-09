package service;

import lombok.RequiredArgsConstructor;
import model.Location;
import org.springframework.stereotype.Service;
import repository.LocationRepository;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public Location getLocationByArea(){

        return null;
    }

    public Location getLocationByCoordinates (){

        return null;
    }
}
