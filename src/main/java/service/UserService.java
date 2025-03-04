package service;

import exception.*;
import lombok.RequiredArgsConstructor;
import model.Session;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.SessionRepository;
import repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final AuthService authService;

    public User saveUser(User user) {
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        user = User.builder()
                .login(user.getLogin())
                .password(hashedPassword)
                .build();
        userRepository.save(user);
        return user;

    }

    public boolean isPasswordCorrect(String password, User user) {
        return BCrypt.checkpw(password, user.getPassword());
    }


    public User getUserBySession(String sessionId) {
        Session session = sessionRepository.getSessionBySessionId(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Session was not found"));

        if (!authService.isSessionRelevant(session)) {
            authService.delete(session);
            throw new SessionAlreadyDeadException("Session is over ");
        }

        return User.builder()
                .id(session.getUserId().getId())
                .login(session.getUserId().getLogin())
                .build();
    }


    public Optional<User> findUserByLogin(String login) {
        return userRepository.getUserByLogin(login);
    }
}
