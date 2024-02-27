package com.epam.epamgymdemo.dao;

import javax.management.InstanceNotFoundException;

public interface DataDeleter {
    void delete(Long id) throws InstanceNotFoundException;
}
