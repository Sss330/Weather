package controller;

import exception.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Session;
import model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.AuthService;
import service.UserService;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    final int MIN_PASSWORD_LENGTH = 3;


    @GetMapping("/sign-up")
    public String signUpAuthorization(Model model) {
        model.addAttribute("user", new User());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpAuthorization(@CookieValue(value = "sessionId", required = false) String sessionId,
                                      @ModelAttribute("user") User user,
                                      @RequestParam String repeatPassword,
                                      HttpServletResponse resp,
                                      Model model) {

        if (!user.getPassword().equals(repeatPassword)) {
            model.addAttribute("error", "Passwords don't match ");
            return "sign-up-with-errors";
        }

      /*  if (user.getPassword().length() <= MIN_PASSWORD_LENGTH) {
            log.error("User selected a busy username  {}", user.getLogin());
            model.addAttribute("error", "Password should be longer than 3 chars ");
            return "sign-up-with-errors";
        }*/

        try {
            User savedUser = userService.saveUser(user.getLogin(), user.getPassword())
                    .orElseThrow(() -> new RuntimeException("User not saved"));

            Session session = authService.makeSession(savedUser)
                    .orElseThrow(() -> new RuntimeException("Session not created"));

            setCookie(resp, session);

        } catch (Exception e) {
            return "redirect:/error";
        }
        return "redirect:/home";
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
        try {
            User existingUser = userService.getUserByLogin(user.getLogin())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            if (!userService.isPasswordCorrect(user.getPassword(), existingUser)) {
                model.addAttribute("error", "Wrong password!");
                return "sign-in-with-errors";
            }
           /* = authService.getSessionByUserId(existingUser.getId())
                    .map(existingSession -> {
                        authService.refreshSession(existingSession);
                        return existingSession;
                    })
                    .orElseGet(() -> authService.makeSession(existingUser)
                            .orElseThrow(() -> new RuntimeException("Не удалось создать сессию"))
                    );
            setCookie(resp, session);*/

                Cookie cookie = new Cookie("sessionId", sessionId);
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                cookie.setMaxAge(3600);
                resp.addCookie(cookie);

        } catch (Exception e) {
            model.addAttribute("error", "Login failed: " + e.getMessage());
            return "sign-in-with-errors";
        }
        return "redirect:/home";
    }

    @PostMapping("/logout")
    public String logout(@CookieValue("sessionId") String sessionId,
                         HttpServletResponse response) {
        try {
            authService.logout(UUID.fromString(sessionId), response);
        } catch (Exception e) {
            return "error";
        }
        return "redirect:/registration/sign-in";
    }

    private void setCookie(HttpServletResponse resp, Session session) {
        Cookie cookie = new Cookie("sessionId", session.getId().toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        resp.addCookie(cookie);
    }
}
