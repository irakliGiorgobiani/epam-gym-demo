package com.epam.epamgymdemo.dao;

import com.epam.epamgymdemo.model.User;
import com.epam.epamgymdemo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import javax.management.InstanceNotFoundException;
import java.util.List;
@Repository
@Slf4j
public class UserDao implements DataCreator<User>, DataSelector<User> {

    private final UserRepository userRepository;

    public UserDao(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void create(User user) {
        if (userRepository.existsById(user.getUserId())) {
            throw new DuplicateKeyException("User with this id already exists");
        } else {
            userRepository.save(user);
            log.info("User successfully created");
        }
    }

    @Override
    public User get(Long id) throws InstanceNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new InstanceNotFoundException(String.format("User not found with the id: %d", id)));
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }
}
