package repository;

import exception.DeletingSessionException;
import exception.SavingSessionException;
import exception.SessionNotFoundException;
import jakarta.transaction.Transactional;
import model.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SessionRepository implements CrudRepository<Session> {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public Optional<Session> getSessionByUserId(Long id) {

        try {
            return Optional.ofNullable(sessionFactory.getCurrentSession()
                    .createQuery("FROM Session WHERE userId = :user_id", Session.class)
                    .setParameter("user_id", id)
                    .uniqueResult()
            );
        } catch (Exception e) {
            throw new SessionNotFoundException("Не удалось найти сессию по айди юзера " + e);
        }
    }
    @Transactional
    public Optional<Session> getSessionById(String sessionId) {

        try {
            return Optional.ofNullable(sessionFactory.getCurrentSession()
                    .createQuery("FROM Session WHERE id = :id", Session.class)
                    .setParameter("id", sessionId)
                    .uniqueResult()
            );
        } catch (Exception e) {
            throw new SessionNotFoundException("Не удалось найти сессию по айди сессии " + e);
        }
    }

    @Transactional
    @Override
    public void save(Session session) throws SavingSessionException {
        try {
            sessionFactory.getCurrentSession().save(session);
        } catch (Exception e) {
            throw new SavingSessionException("Не удалось сохранить сессию " + e);
        }
    }

    @Transactional
    @Override
    public Optional<List<Session>> findAll() {
        try {
            return Optional.ofNullable(sessionFactory.getCurrentSession()
                    .createQuery("FROM Session", Session.class)
                    .getResultList()
            );
        } catch (Exception e) {
            throw new SessionNotFoundException("Не удалось найти все сессии " + e);
        }
    }

    @Transactional
    @Override
    public void delete(Session session) {
        try {
            sessionFactory.getCurrentSession().delete(session);
        } catch (Exception e) {
            throw new DeletingSessionException("Не удалось удалить сессию " + e);
        }
    }
}

