package repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {

    void save (T t);

    List<T> findAll();

    void delete (T t);
}
