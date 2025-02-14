package service;

import exception.DeletingUserException;
import exception.SavingUserException;
import exception.UserAlreadyExistException;
import exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public Optional<User>  saveUser(String login, String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        try {
            User user = User.builder()
                    .login(login)
                    .password(hashedPassword)
                    .build();
            userRepository.save(user);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            throw new SavingUserException("Can`t save user " + e);
        }
    }

    public void deleteUser(User user) {
        try {
            userRepository.delete(user);
        } catch (Exception e) {
            throw new DeletingUserException("Can`t delete user " + e);
        }
    }

    public boolean isPasswordCorrect(String password, User user) {
        return BCrypt.checkpw(password, user.getPassword());
    }
    
    public Optional<User> getUserByLogin(String login) {
        try {
            return userRepository.getUserByLogin(login);

        } catch (UserNotFoundException e) {
            throw new UserNotFoundException("Can`t find user by login  " + e);
        }
    }
}
