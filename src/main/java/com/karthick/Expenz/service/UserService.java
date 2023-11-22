package com.karthick.Expenz.service;

import com.karthick.Expenz.entity.User;
import com.karthick.Expenz.exception.BadRequestException;
import com.karthick.Expenz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Cacheable(value = "user", key = "#id")
    public User findUserById(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NoSuchElementException("expecting user is not found");
        }
        return user.get();
    }

    public User createNewUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }

    @CacheEvict(value = "user", key = "#id")
    public User updateUserByFields(long id, Map<String, Object> fields) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NoSuchElementException("expecting user is not found");
        }
        try {
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(User.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, user.get(), value);
                }
            });
            return userRepository.save(user.get());
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }

    @CacheEvict(value = "user", key = "#id")
    public String deleteUserById(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NoSuchElementException("expecting user is not found");
        }
        userRepository.delete(user.get());
        return "user has been deleted";
    }
}
