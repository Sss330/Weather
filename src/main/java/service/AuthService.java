package service;

import exception.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import model.Session;
import model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.SessionRepository;
import repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;


    public Session signUp(User user) throws SavingSessionException {
        try {
            UUID sessionId = UUID.randomUUID();

            Session session = Session.builder()
                    .id(sessionId)
                    .userId(user)
                    //one month
                    .expiresAt(LocalDateTime.now().plusHours(744))
                    .build();

            sessionRepository.save(session);
            return session;
        } catch (Exception e) {
            throw new SavingSessionException("Can`t create session fot user " + e);
        }
    }

    public boolean isSessionRelevant(Session session) throws SavingSessionException {
        try {
            return session.getExpiresAt().isAfter(LocalDateTime.now());
        } catch (Exception e) {
            throw new SessionAlreadyDeadException("Time for using this session is over " + e);
        }
    }


    public void logout(UUID sessionId, HttpServletResponse response) throws DeletingSessionException {
        try {
            Cookie cookie = new Cookie("sessionId", sessionId.toString());
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);


            Session session = sessionRepository.getSessionBySessionId(sessionId)
                    .orElseThrow(() -> new SessionNotFoundException("Session is not find"));
            sessionRepository.delete(session);
        } catch (Exception e) {
            throw new DeletingSessionException("Can`t logout of account " + e);
        }
    }
    public void delete(Session session){
        try {
            sessionRepository.delete(session);
        } catch (DeletingSessionException e) {
            throw new DeletingSessionException("Can`t deleting session " + e);
        }
    }

    public boolean isSessionAlreadyExist(Long id) {

        try {
            return sessionRepository.isSessionAlreadyExist(id);
        } catch (Exception e) {
            throw new SessionAlreadyExistException("Can`t check sessions " + e);
        }
    }

    public Optional<Session> getSessionByUserId(Long id) {

        try {
            return sessionRepository.getSessionByUserId(id);
        } catch (Exception e) {
            throw new SessionNotFoundException("Can`t find sessions " + e);
        }
    }


}
