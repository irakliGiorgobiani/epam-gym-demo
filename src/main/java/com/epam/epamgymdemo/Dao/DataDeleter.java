package com.epam.epamgymdemo.Dao;

import javax.management.InstanceNotFoundException;

public interface DataDeleter {
    void delete(Long id) throws InstanceNotFoundException;
}
