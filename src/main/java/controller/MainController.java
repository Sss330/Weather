package controller;

import dto.api.SearchQuery;
import dto.api.WeatherResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Location;
import model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.LocationService;
import service.UserService;

import java.util.List;


@RequiredArgsConstructor
@Slf4j
@Controller
public class MainController {

    private final UserService userService;
    private final LocationService locationService;

    @GetMapping("/")
    public String home(@CookieValue(value = "sessionId", required = false)String sessionId, Model model){

        if (sessionId == null || sessionId.isBlank()){
            return "redirect:/registration/sign-up";
        }

        model.addAttribute("searchQuery", new SearchQuery());
        User user = userService.getUserBySession(sessionId);

        List<Location> locations = locationService.getUsersLocations(user);
        List<WeatherResponseDto> weatherData = locations.stream()
                .map(loc -> locationService.getLocationByCoordinates(loc.getLatitude(), loc.getLongitude()))
                .toList();

        model.addAttribute("name", user.getLogin());
        model.addAttribute("locations", locations);
        model.addAttribute("weatherData", weatherData);

        return "index";
    }

    @PostMapping("/delete-location")
    public String deleteLocation(@CookieValue(value = "sessionId", required = false)String sessionId,
                                 @RequestParam("locationId") Long locationId){

        if (sessionId == null || sessionId.isBlank()){
            return "redirect:/registration/sign-up";
        }

        User user = userService.getUserBySession(sessionId);
        locationService.deleteLocationById(user, locationId);
        return "redirect:/";

    }
}
