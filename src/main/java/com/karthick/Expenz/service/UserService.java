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
public class UserService {
  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;

  @Cacheable(value = "user", key = "#id")
  public User findUser(long id) {
    Optional<User> user = userRepository.findById(id);
    if (user.isPresent()) {
      return user.get();
    }
    throw new EntityNotFoundException(id, User.class);
  }

  public User findUserByUsername(String username) {
    Optional<User> user = userRepository.findByUsername(username);
    if (user.isPresent()) {
      return user.get();
    }
    throw new EntityNotFoundException(
        "The user with the username '" + username + "' does not exist in our records");
  }

  public User createUser(User user) {
    try {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      return userRepository.save(user);
    } catch (Exception ex) {
      throw new BadRequestException(ex.getMessage());
    }
  }

  @CacheEvict(value = "user", key = "#id")
  public User updateUser(long id, Map<String, Object> fields) {
    User user = findUser(id);
    try {
      fields.forEach(
          (key, value) -> {
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

  @CacheEvict(value = "user", key = "#id")
  public void deleteUser(long id) {
    if (userRepository.existsById(id)) {
      userRepository.deleteById(id);
    }
    throw new EntityNotFoundException(id, User.class);
  }
}
