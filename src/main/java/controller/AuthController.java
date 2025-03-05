package controller;

import exception.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import model.Session;
import model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.AuthService;
import service.UserService;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private static final int COOKIE_MAX_AGE = 3600;
    private static final int COOKIE_MIN_AGE = 0;


    @GetMapping("/sign-up")
    public String signUpAuthorization(Model model) {
        model.addAttribute("user", new User());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpAuthorization(@CookieValue(value = "sessionId", required = false) String sessionId,
                                      @ModelAttribute("user") User user,
                                      @RequestParam("repeatPassword") String repeatPassword,
                                      HttpServletResponse resp,
                                      Model model) {

        if (sessionId != null) {
            return "redirect:/";
        }

        if (!user.getPassword().equals(repeatPassword)) {
            model.addAttribute("error", "Passwords don't match ");
            return "sign-up-with-errors";
        }


        if (user.getLogin() == null || user.getLogin().isBlank() || user.getPassword().isBlank()) {
            model.addAttribute("error", "Login and password is required");
            return "sign-up-with-errors";
        }

        try {
            User savedUser = userService.saveUser(user);
            Session session = authService.makeSession(savedUser)
                    .orElseThrow(() -> new RuntimeException("Session is`t saved"));

            setCookie(resp, session);

            return "redirect:/";
        } catch (SignUpException e) {
            return "error";
        }
    }

    @GetMapping("/sign-in")
    public String signInAuthorization(Model model) {
        model.addAttribute("user", new User());
        return "sign-in";
    }

    @PostMapping("/sign-in")
    public String signIn(@CookieValue(value = "sessionId", required = false) String sessionId,
                         @ModelAttribute("user") User user,
                         HttpServletResponse resp,
                         Model model) {
        if (sessionId != null) {
            return "redirect:/";
        }

        try {
            User existingUser = userService.findUserByLogin(user.getLogin())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            if (!userService.isPasswordCorrect(user.getPassword(), existingUser)) {
                model.addAttribute("error", "Wrong password!");
                return "sign-in-with-errors";
            }

            Optional<Session> existingSession = authService.getSessionByUserId(existingUser);
            Session session;

            if (existingSession.isPresent()) {
                session = existingSession.get();
                authService.refreshSession(session);
            } else {
                Optional<Session> newSession = authService.makeSession(existingUser);
                if (newSession.isEmpty()) {
                    model.addAttribute("error", "Can`t create session, pls try again");
                    return "sign-in-with-errors";
                }
                session = newSession.get();
            }
            setCookie(resp, session);
            return "redirect:/";

        } catch (Exception e) {
            return "error";
        }
    }

    @PostMapping("/logout")
    public String logout(@CookieValue("sessionId") String sessionId,
                         HttpServletResponse response) {

        if (sessionId == null || sessionId.isBlank()) {
            return "redirect:/registration/sign-in";
        }

        try {
            authService.logout(UUID.fromString(sessionId), response);
            deleteCookie(response);
        } catch (Exception e) {
            return "error";
        }
        return "redirect:/registration/sign-in";
    }

    private void setCookie(HttpServletResponse resp, Session session) {
        Cookie cookie = new Cookie("sessionId", session.getId().toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(COOKIE_MAX_AGE);
        resp.addCookie(cookie);
    }

    private void deleteCookie(HttpServletResponse resp) {
        Cookie cookie = new Cookie("sessionId", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(COOKIE_MIN_AGE);
        resp.addCookie(cookie);
    }
}
