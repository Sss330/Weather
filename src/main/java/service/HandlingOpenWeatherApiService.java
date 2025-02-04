package service;

import model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.LocationRepository;

@Service
public class HandlingOpenWeatherApiService {

    @Autowired
    LocationRepository locationRepository;

    public Location getLocationByArea(){

        return null;
    }

    public Location getLocationByCoordinates (){

        return null;
    }
}
