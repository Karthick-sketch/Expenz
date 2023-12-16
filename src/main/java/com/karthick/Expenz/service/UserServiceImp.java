package com.karthick.Expenz.service;

import com.karthick.Expenz.entity.User;
import com.karthick.Expenz.exception.BadRequestException;
import com.karthick.Expenz.exception.EntityNotFoundException;
import com.karthick.Expenz.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImp implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    @Cacheable(value = "user", key = "#id")
    public User getUserById(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new EntityNotFoundException(id, User.class);
        }
    }

    @Override
    public User getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new EntityNotFoundException("The user with the username '" + username + "' does not exist in our records");
        }
    }

    @Override
    public User createNewUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }

    @Override
    @CacheEvict(value = "user", key = "#id")
    public User updateUserByFields(long id, Map<String, Object> fields) {
        User user = getUserById(id);
        try {
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(User.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, user, value);
                }
            });
            return userRepository.save(user);
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }

    @Override
    @CacheEvict(value = "user", key = "#id")
    public void deleteUserById(long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(id, User.class);
        }
    }
}
