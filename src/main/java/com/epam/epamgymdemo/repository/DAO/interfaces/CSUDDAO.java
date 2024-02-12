package com.epam.epamgymdemo.repository.DAO.interfaces;

public interface CSUDDAO<T> extends CSUDAO<T> {
    void delete(String username);
}
