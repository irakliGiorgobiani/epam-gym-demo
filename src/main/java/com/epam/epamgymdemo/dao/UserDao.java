package com.epam.epamgymdemo.dao;

import com.epam.epamgymdemo.model.User;
import com.epam.epamgymdemo.repository.UserRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceNotFoundException;
import java.util.List;
@Repository
public class UserDao implements DataCreator<User>, DataSelector<User>, DataDeleter {

    private final UserRepository userRepository;

    public UserDao(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void create(User user) {
        userRepository.save(user);
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
    public void updateUsername(Long id, String username) throws InstanceNotFoundException {
        User user = this.get(id);

        user.setUserName(username);

        userRepository.save(user);
    }

    @Transactional
    public void updateFirstName(Long id, String firstName) throws InstanceNotFoundException {
        User user = this.get(id);

        user.setFirstName(firstName);

        userRepository.save(user);
    }

    @Transactional
    public void updateLastName(Long id, String lastName) throws InstanceNotFoundException {
        User user = this.get(id);

        user.setLastName(lastName);

        userRepository.save(user);
    }

    @Transactional
    public void updateIsActive(Long id, Boolean isActive) throws InstanceNotFoundException {
        User user = this.get(id);

        user.setIsActive(isActive);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(Long id) throws InstanceNotFoundException {
        if (!userRepository.existsById(id)) {
            throw new InstanceNotFoundException(String.format("User not found with the id: %d", id));
        } else {
            userRepository.deleteById(id);
        }
    }
}
