package com.karthick.Expenz.controller;

import com.karthick.Expenz.entity.User;
import com.karthick.Expenz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UsersController {
    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAllUsers());
    }

    @GetMapping("/user/{user-id}")
    public ResponseEntity<User> getUserById(@PathVariable("user-id") long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findUserById(id));
    }

    @PostMapping("/user")
    public ResponseEntity<User> createNewUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createNewUser(user));
    }

    @PatchMapping("/user/{user-id}")
    public ResponseEntity<User> updateUserById(@PathVariable("user-id") long id, @RequestBody Map<String, Object> updatedUser) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserByFields(id, updatedUser));
    }

    @DeleteMapping("/user/{user-id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("user-id") long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userService.deleteUserById(id));
    }
}
