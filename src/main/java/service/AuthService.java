package service;

import exception.DeletingSessionException;
import exception.SavingSessionException;
import exception.SessionNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import model.Session;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.SessionRepository;
import repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthService {

    SessionRepository sessionRepository;

    UserRepository userRepository;

    @Autowired
    public AuthService(SessionRepository sessionRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    public void signUp(User user, HttpServletResponse response) throws SavingSessionException {
        try {
            UUID sessionId = UUID.randomUUID();

            Cookie cookie = new Cookie("sessionId", sessionId.toString());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(3600);
            response.addCookie(cookie);

            Session session = Session.builder()
                    .id(UUID.fromString(sessionId.toString()))
                    .userId(user)
                    .expiresAt(LocalDateTime.now().plusHours(1))
                    .build();

            sessionRepository.save(session);
        } catch (Exception e) {
            throw new SavingSessionException("Не удалось создать сессию для юзера " + e);
        }
    }

    public void logout(UUID sessionId, HttpServletResponse response) throws DeletingSessionException {
        try {
            Cookie cookie = new Cookie("sessionId", sessionId.toString());
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);


            Session session = sessionRepository.getSessionById(sessionId.toString())
                    .orElseThrow(() -> new SessionNotFoundException("Сессия не найдена"));
            sessionRepository.delete(session);
        } catch (Exception e) {
            throw new DeletingSessionException("Не удалось удалить сессию " + e);
        }
    }

}
