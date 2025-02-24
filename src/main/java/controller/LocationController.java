package controller;

import dto.api.LocationResponseDto;
import dto.api.SearchQuery;
import dto.api.WeatherResponseDto;
import exception.SavingLocationException;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Location;
import model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.LocationService;
import service.UserService;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("/location")
public class LocationController {

    private final LocationService locationService;
    private final UserService userService;

    @GetMapping("/search")
    public String searchLocation(Model model) {
        model.addAttribute("searchQuery", new SearchQuery());
        return "/search-results";
    }

    @PostMapping("/search-area")
    public String searchLocationByArea(@CookieValue(value = "sessionId", required = false) String sessionId,
                                       @ModelAttribute SearchQuery searchQuery,
                                       Model model) {

        if (sessionId == null || sessionId.isBlank()) {
            return "redirect:/registration/sign-in";
        }
        log.info("area parameter = {}", searchQuery);

        User user = userService.getUserBySession(sessionId);
        List<WeatherResponseDto> locations = locationService.getLocationByArea(searchQuery);
        searchQuery.setArea(searchQuery.getArea());

        model.addAttribute("locations", locations);
        model.addAttribute("name", user.getLogin());
        model.addAttribute("searchQuery", searchQuery);

        return "search-results";
    }


    @PostMapping("/search-coordinates")
    public String searchLocationByCoordinates(@CookieValue(value = "sessionId", required = false) String sessionId,
                                              @RequestParam @DecimalMin("-90") @DecimalMax("90") BigDecimal lat,
                                              @RequestParam @DecimalMin("-180") @DecimalMax("180") BigDecimal lon,
                                              Model model) {
        try {
            if (sessionId == null || sessionId.isBlank()) {
                return "redirect:/registration/sign-in";
            }
            LocationResponseDto location = locationService.getLocationByCoordinates(lat, lon);
            model.addAttribute("location", location);

        } catch (Exception e) {
            model.addAttribute("error", "Location not found.");
        }
        return "redirect:/search-results";
    }

    @PostMapping("/add-location")
    public String addLocation(@CookieValue(value = "sessionId", required = false) String sessionId,
                              @ModelAttribute("location") Location location) {
        try {
            locationService.saveLocation(location);
        } catch (Exception e) {
            throw new SavingLocationException("Can`t save location" + e.getMessage());
        }

        return "redirect:/";
    }

}
