package com.karthick.Expenz.service;

import com.karthick.Expenz.common.ApiResponse;
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
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ApiResponse findAllUsers() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(userRepository.findAll());
        return apiResponse;
    }

    @Cacheable(value = "user", key = "#id")
    public User findUserById(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NoSuchElementException("expecting user is not found");
        }
        return user.get();
    }

    public ApiResponse createNewUser(User user) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            apiResponse.setData(userRepository.save(user));
        } catch (AssertionError e) {
            throw new BadRequestException(e.getMessage());
        }
        return apiResponse;
    }

    @CacheEvict(value = "user", key = "#id")
    public ApiResponse updateUserByFields(long id, Map<String, Object> fields) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NoSuchElementException("expecting user is not found");
        }
        ApiResponse apiResponse = new ApiResponse();
        try {
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(User.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, user.get(), value);
                }
            });
            apiResponse.setData(userRepository.save(user.get()));
        } catch (AssertionError e) {
            throw new BadRequestException(e.getMessage());
        }
        return apiResponse;
    }

    @CacheEvict(value = "user", key = "#id")
    public ApiResponse deleteUserById(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NoSuchElementException("expecting user is not found");
        }

        userRepository.delete(user.get());
        ApiResponse apiResponse = new ApiResponse();
        if (userRepository.findById(id).isEmpty()) {
            apiResponse.setData("user has been deleted");
        } else {
            throw new BadRequestException("problem with deleting the user");
        }

        return apiResponse;
    }
}
