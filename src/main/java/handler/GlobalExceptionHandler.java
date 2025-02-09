package handler;

import exception.SignInException;
import exception.SignUpException;
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

    /*@ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationExceptions(MethodArgumentNotValidException ex, Model model) {
        String errorMsg = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        model.addAttribute("error", errorMsg);
        return "error-page";
    }*/
}