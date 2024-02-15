package com.epam.epamgymdemo.Dao;

import javax.management.InstanceNotFoundException;

public interface DataUpdater<T> {
    void update(T t) throws InstanceNotFoundException;
}
