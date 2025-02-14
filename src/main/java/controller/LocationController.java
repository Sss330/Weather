package controller;

import dto.api.SearchQuery;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Location;
import model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.AuthService;
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
    private final AuthService authService;

    @GetMapping("/search")
    public String searchLocation(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("searchQuery", new SearchQuery());
        model.addAttribute("location", new Location());
        return "/search-results";
    }

    @PostMapping("/search-area")
    public String searchLocationByArea(@CookieValue(value = "sessionId", required = false) String sessionId,
                                       @ModelAttribute("searchQuery") SearchQuery searchQuery,
                                       Model model,
                                       @RequestParam String area) {

        try {
            if (sessionId == null) {
                return "redirect:/registration/sign-up";
            }

            locationService.saveLocation(locationService.getLocationByArea(area));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/search-results";
    }


    @PostMapping("/search-coordinates")
    public String searchLocationByCoordinates(@CookieValue(value = "sessionId", required = false) String sessionId,
                                              @ModelAttribute("user") User user,
                                              @RequestParam BigDecimal lat, BigDecimal lon,
                                              HttpSession session,
                                              Model model) {
        try {

            List<Location> results = locationService.ge(lat, lon);
            session.setAttribute("searchResults", results);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/search-results";
    }

    @DeleteMapping("/delete")
    public String delete() {

        return "redirect:/search-results";
    }
}
