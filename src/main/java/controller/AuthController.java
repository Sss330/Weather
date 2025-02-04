package controller;

import exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.AuthService;
import service.UserService;


@Controller

public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;


    @GetMapping("/authorization")
    public String authorization() {
        return "sign-up";
    }

    @PostMapping("/authorization")
    public String authorization(@RequestParam String login, @RequestParam String password,
                                HttpServletResponse resp, HttpServletRequest request) {

        authService.signUp(userService.findUserByLogin(login).orElseThrow(()-> new UserNotFoundException("Не удалось найти юзера")), resp);

        return "sign-up";
    }
}
