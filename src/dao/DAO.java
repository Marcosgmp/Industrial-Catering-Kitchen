package dao;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {

    Optional<T> findById(Integer id);

    List<T> findAll();

    void insert(T entity);

    void update(T entity);

    void delete(Integer id);
}