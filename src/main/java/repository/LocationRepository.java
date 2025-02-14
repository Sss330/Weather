package repository;

import exception.DeletingLocationException;
import exception.SavingLocationException;
import lombok.RequiredArgsConstructor;
import model.Location;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LocationRepository implements CrudRepository<Location> {

    private final SessionFactory sessionFactory;

    public void deleteById(Long id) {
        try {
            Location location = sessionFactory.getCurrentSession()
                    .createQuery("FROM Location WHERE userId = :id", Location.class)
                    .setParameter("user_id", id)
                    .uniqueResult();
            if (location != null) {
                sessionFactory.getCurrentSession().delete(location);
            }
        } catch (Exception e) {
            throw new DeletingLocationException("Не удалось удалить локацию " + e);
        }
    }

    public List<Location> findByUserId(Long id) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Location WHERE userId = :id", Location.class)
                .setParameter("user_id", id)
                .getResultList();
    }

    @Override
    public void save(Location location) {
        try {
            sessionFactory.getCurrentSession().save(location);
        } catch (HibernateException e) {
            throw new SavingLocationException("Не удалось сохранить локацию " + e);
        }
    }

    @Override
    public Optional<List<Location>> findAll() {
        try {
            return Optional.ofNullable(sessionFactory.getCurrentSession()
                    .createQuery("FROM Location", Location.class)
                    .getResultList()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Location location) {
        try {
            sessionFactory.getCurrentSession().delete(location);
        } catch (Exception e) {
            throw new DeletingLocationException("Не удалось удалить локацию " + e);
        }
    }
}
