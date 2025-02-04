package service;

import exception.DeletingUserException;
import exception.SavingUserException;
import exception.UserAlreadyExistException;
import exception.UserNotFoundException;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.UserRepository;

import java.util.Optional;

@Service

public class UserService {

    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(String login, String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        try {
            User user = User.builder()
                    .login(login)
                    .password(hashedPassword)
                    .build();
            if (userRepository.findUserByLogin(login).isPresent()) {
                throw new UserAlreadyExistException("Юзера с данным логином уже существует ");
            }
            userRepository.save(user);
        } catch (Exception e) {
            throw new SavingUserException("Не удалось сохранить юзера " + e);
        }
    }

    public void saveUser(User user) {
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new SavingUserException("Не удалось сохранить юзера " + e);
        }
    }

    public void deleteUser(User user) {
        try {
            userRepository.delete(user);
        } catch (Exception e) {
            throw new DeletingUserException("Не удалось удалить юзера " + e);
        }
    }

    public Optional<User> findUserByLogin(String login) {
        try {
            return userRepository.findUserByLogin(login);

        } catch (UserNotFoundException e) {
            throw new UserNotFoundException("Не удалось найти юзера по логину " + e);
        }
    }

}
