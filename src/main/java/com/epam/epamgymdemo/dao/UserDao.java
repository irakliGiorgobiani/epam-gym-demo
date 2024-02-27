package com.epam.epamgymdemo.dao;

import com.epam.epamgymdemo.model.User;
import com.epam.epamgymdemo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void create(User user) {
        if (userRepository.existsById(user.getId())) {
            throw new DuplicateKeyException(String.format("User with the id: %d already exists", user.getId()));
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

    public User getByUsername(String username) throws InstanceNotFoundException {
        if (!userRepository.existsByUserName(username)) {
            throw new InstanceNotFoundException(String.format("User not found with the username: %s", username));
        } else return userRepository.findByUserName(username);
    }

    @Transactional
    public void updatePassword(Long id, String password) throws InstanceNotFoundException {
        User user = this.get(id);

        user.setPassword(password);

        userRepository.save(user);
    }

    @Transactional
    public void updateIsActive(Long id, Boolean isActive) throws InstanceNotFoundException {
        User user = this.get(id);

        user.setIsActive(isActive);

        userRepository.save(user);
    }
}
