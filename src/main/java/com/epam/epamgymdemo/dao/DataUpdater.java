package com.epam.epamgymdemo.dao;

import javax.management.InstanceNotFoundException;

public interface DataUpdater<T> {
    void update(T t) throws InstanceNotFoundException;
}
