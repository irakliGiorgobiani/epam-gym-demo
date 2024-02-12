package com.epam.epamgymdemo.repository.DAO.interfaces;

import java.util.Map;

public interface CSDAO<T> {
    T get(String username);
    Map<String,T> getAll();
    void create(T t);
}
