package repository;

import exception.UserNotFoundException;

import java.util.List;
import java.util.Optional;

public interface CrudWeatherRepository<T> {

    void save (T t);
    Optional<List<T>> findAll();
    void delete (T t);
}
