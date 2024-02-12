package com.epam.epamgymdemo.repository.DAO.interfaces;

public interface CSUDAO<T> extends CSDAO<T> {
    void update(T t);
}
