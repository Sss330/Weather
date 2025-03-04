package controller;

import dto.api.SearchQuery;
import dto.api.WeatherResponseDto;
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
        try {
            log.info("area parameter = {}", searchQuery);

            User user = userService.getUserBySession(sessionId);
            List<WeatherResponseDto> locations = locationService.getLocationByArea(searchQuery);
            searchQuery.setArea(searchQuery.getArea());

            model.addAttribute("locations", locations);
            model.addAttribute("name", user.getLogin());
            model.addAttribute("searchQuery", searchQuery);

            return "search-results";
        } catch (Exception e) {
            return "error";
        }
    }


    @PostMapping("/search-coordinates")
    public String searchLocationByCoordinates(@CookieValue(value = "sessionId", required = false) String sessionId,
                                              @RequestParam(name = "lat") @DecimalMin("-90") @DecimalMax("90") BigDecimal lat,
                                              @RequestParam(name = "lon") @DecimalMin("-180") @DecimalMax("180") BigDecimal lon,
                                              Model model) {
        try {
            if (sessionId == null || sessionId.isBlank()) {
                return "redirect:/registration/sign-in";
            }

            User user = userService.getUserBySession(sessionId);
            WeatherResponseDto location = locationService.getLocationByCoordinates(lat, lon);

            model.addAttribute("locations", List.of(location));
            model.addAttribute("name", user.getLogin());

        } catch (Exception e) {
            return "error";
        }
        return "search-results";
    }

    @PostMapping("/add-location")
    public String addLocation(@CookieValue(value = "sessionId", required = false) String sessionId,
                              @ModelAttribute("location") Location location) {
        if (sessionId == null || sessionId.isBlank()) {
            return "redirect:/registration/sign-in";
        }

        try {
            User user = userService.getUserBySession(sessionId);
            location.setUserId(user);
            locationService.saveLocation(location);
        } catch (Exception e) {
            return "error";
        }

        return "redirect:/";
    }

}