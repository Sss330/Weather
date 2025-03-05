package handler;

import exception.SessionNotFoundException;
import exception.SignInException;
import exception.SignUpException;
import exception.UserNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SignUpException.class)
    public String handleSignUpException(SignUpException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "sign-up-with-errors";
    }

    @ExceptionHandler(SignInException.class)
    public String handleSignInException(SignInException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "sign-in-with-errors";
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintException(SignInException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "sign-in-with-errors";
    }

    @ExceptionHandler({UserNotFoundException.class, SessionNotFoundException.class})
    public String handleLoginExceptions(Exception ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "sign-in-with-errors";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralExceptions(Exception ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }
}