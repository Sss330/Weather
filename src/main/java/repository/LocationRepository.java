package repository;

import exception.DeletingLocationException;
import exception.SavingLocationException;
import model.Location;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class LocationRepository implements CrudRepository<Location> {

    private SessionFactory sessionFactory;

    @Autowired
    public LocationRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
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
