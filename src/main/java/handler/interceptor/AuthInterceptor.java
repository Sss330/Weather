package handler.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import model.Session;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import service.AuthService;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    //check authorization
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        if (request.getRequestURI().startsWith("/registration")) {
            return true;
        }

        Optional<Cookie> sessionCookie = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("sessionId"))
                .findFirst();

        if (sessionCookie.isEmpty()) {
            response.sendRedirect("/registration/sign-in");
            return false;
        }

        UUID sessionId = UUID.fromString(sessionCookie.get().getValue());
        Optional<Session> session = authService.getSessionBySessionId(sessionId);

        if (session.isEmpty() || !authService.isSessionRelevant(session.get())) {
            response.sendRedirect("/registration/sign-in");
            return false;
        }

        return true;
    }
}