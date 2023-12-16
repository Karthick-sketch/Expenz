package com.karthick.Expenz.service;

import com.karthick.Expenz.entity.User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface UserService {
   User getUserById(long id);

    User getUserByUsername(String name);

   User createNewUser(User user);

   User updateUserByFields(long id, Map<String, Object> fields);

   void deleteUserById(long id);
}
