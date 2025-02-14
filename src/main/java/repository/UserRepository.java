package repository;

import dto.UserDto;
import exception.DeletingUserException;
import exception.SavingUserException;
import exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import model.User;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository implements CrudRepository<User> {

    private final SessionFactory sessionFactory;

    @Transactional()
    public Optional<User> getUserByLogin(String login) throws UserNotFoundException {
        try {
            return Optional.ofNullable(
                    sessionFactory.getCurrentSession()
                            .createQuery("FROM User WHERE login = :login", User.class)
                            .setParameter("login", login)
                            .uniqueResult()
            );
        } catch (Exception e) {
            throw new UserNotFoundException("Не удалось найти юзера по логину " + e);
        }
    }

    @Transactional
    @Override
    public void save(User user) throws SavingUserException {
        try {
            sessionFactory.getCurrentSession().save(user);
        } catch (Exception e) {
            throw new SavingUserException("Can`t save user " + e);
        }
    }

    @Transactional
    @Override
    public Optional<List<User>> findAll() throws UserNotFoundException {
        try {
            return Optional.ofNullable(sessionFactory
                    .getCurrentSession()
                    .createQuery("FROM User ", User.class)
                    .getResultList());
        } catch (Exception e) {
            throw new UserNotFoundException("Не удалось найти всех юзеров" + e);
        }
    }

    @Transactional
    @Override
    public void delete(User user) throws DeletingUserException {
        try {
            sessionFactory.getCurrentSession().delete(user);
        } catch (Exception e) {
            throw new DeletingUserException("Не удалось удалить юзера " + e);
        }

    }
}