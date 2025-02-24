package repository;


import exception.DeletingLocationException;
import exception.SavingLocationException;
import lombok.RequiredArgsConstructor;
import model.Location;

import model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class LocationRepository implements CrudRepository<Location> {

    private final SessionFactory sessionFactory;


    @Transactional
    public void deleteByCoordinates(User userId, String name) {
        Session session = sessionFactory.getCurrentSession();
        session.createQuery("DELETE FROM Location q WHERE q.userId = :userId AND q.name = :name")
                .setParameter("userId", userId)
                .setParameter("name", name)
                .executeUpdate();

    }
    @Transactional
    public List<Location> findLocationsByUserId(User userId) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Location A WHERE A.userId = :userId",Location.class)
                .setParameter("userId", userId)
                .getResultList();
    }
    @Transactional
    @Override
    public void save(Location location) {
        try {
            sessionFactory.getCurrentSession().save(location);
        } catch (HibernateException e) {
            throw new SavingLocationException("Не удалось сохранить локацию " + e);
        }
    }
    @Transactional
    @Override
    public List<Location> findAll() {
        try {
            return sessionFactory.getCurrentSession()
                    .createQuery("FROM Location", Location.class)
                    .getResultList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Transactional
    @Override
    public void delete(Location location) {
        try {
            sessionFactory.getCurrentSession().delete(location);
        } catch (Exception e) {
            throw new DeletingLocationException("Не удалось удалить локацию " + e);
        }
    }
}
