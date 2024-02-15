package com.epam.epamgymdemo.Dao;

import javax.management.InstanceNotFoundException;
import java.util.List;

public interface DataSelector<T> {
    T get(Long id) throws InstanceNotFoundException;
    List<T> getAll();
}
