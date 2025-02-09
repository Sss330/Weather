package controller;

import exception.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Session;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.AuthService;
import service.UserService;

import java.util.Optional;



@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/registration/")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    //the password must be short for ease testing
    final int MIN_PASSWORD_LENGTH = 3;


    @GetMapping("/sign-up")
    public String signUpAuthorization(Model model) {
        model.addAttribute("user", new User());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpAuthorization(@ModelAttribute("user") User user,
                                      @RequestParam String repeatPassword,
                                      HttpServletResponse resp, Model model) {

        //remove to handler
        if (!user.getPassword().equals(repeatPassword)) {
            log.error("Passwords don't match {} and {}", user.getPassword(), repeatPassword);
            model.addAttribute("error", "Passwords don't match ");
            return "sign-up-with-errors";
        }

        if (userService.isUserAlreadyExist(user.getLogin())) {
            log.error("User selected a busy username  {}", user.getLogin());
            model.addAttribute("error", "This username already using. Please choose different username, or sign in you`r account ");
            return "sign-up-with-errors";
        }

        if (user.getPassword().length() <= MIN_PASSWORD_LENGTH) {
            log.error("User selected a busy username  {}", user.getLogin());
            model.addAttribute("error", "Password should be longer than 3 chars ");
            return "sign-up-with-errors";
        }


        try {

            userService.saveUser(user.getLogin(), user.getPassword());
            User savedUser = userService.getUserByLogin(user.getLogin())
                    .orElseThrow(() -> new RuntimeException("User not found after saving"));
            log.info("User successfully saved with login: {}", user.getLogin());

            Session session = authService.signUp(savedUser);
            log.info("Session created for user with ID: {}", savedUser.getId());

            Cookie cookie = new Cookie("sessionId", session.getId().toString());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(3600);
            resp.addCookie(cookie);

        } catch (Exception e) {
            log.error("Failed to sign up", e);
            throw new SignUpException("Can`t sign up" + e);
        }
        return "redirect:/search-results";
    }

    @GetMapping("/sign-in")
    public String signInAuthorization(Model model) {
        model.addAttribute("user", new User());
        return "sign-in";
    }

    @PostMapping("/sign-in")
    public String signIn(@ModelAttribute("user") User user,
                         HttpServletResponse resp,
                         Model model) {
        try {
            //remove to handler
            if (authService.getSessionByUserId(user.getId()).isEmpty()) {
                log.error("Can`t find session {}", user.getLogin());
                model.addAttribute("error", "Can`t find your session :( ");
                return "sign-in-with-errors";
            }
            if (!authService.isSessionRelevant(authService.getSessionByUserId(user.getId()).get())) {
                log.error("Deleting session because session is over {}", user.getLogin());
                model.addAttribute("error", "Sorry yor`r session is over, please sign-up again :( ");
                authService.delete(authService.getSessionByUserId(user.getId()).get());
                return "sign-in-with-errors";
            }
            if (userService.getUserByLogin(user.getLogin()).isEmpty()) {
                log.error("User account not found {}", user.getLogin());
                model.addAttribute("error", "Sorry we were not found yor`r account, please sign-up again :( ");
                return "sign-in-with-errors";
            }

            User existingUser = userService.getUserByLogin(user.getLogin())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            if (user.getPassword() == null || !BCrypt.checkpw(user.getPassword(), existingUser.getPassword())) {
                log.error("User insert wrong password {}", user.getPassword());
                model.addAttribute("error", "Wrong password!");
                return "sign-in-with-errors";
            }
            Optional<Session> existingSession = authService.getSessionByUserId(existingUser.getId());

            Cookie cookie = new Cookie("sessionId", existingSession.get().getId().toString());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(3600);
            resp.addCookie(cookie);

        } catch (Exception e) {
            log.error("Failed to sign in", e);
            throw new SignInException("Can`t sign-in " + e);
        }
        return "redirect:/search-results";
    }
}
