package controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import service.LocationService;

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("/search-location")
public class LocationsController {

    private final LocationService locationService;


}
