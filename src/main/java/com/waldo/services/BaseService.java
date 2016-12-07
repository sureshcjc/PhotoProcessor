package com.waldo.services;

import java.util.List;

public interface BaseService<T> {
    List<?> getAll();

    T getById(Integer id);

    T save(T obj);

    void delete(Integer id);
}
