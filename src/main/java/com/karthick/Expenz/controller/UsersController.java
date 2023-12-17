package com.karthick.Expenz.controller;

import com.karthick.Expenz.entity.User;
import com.karthick.Expenz.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UsersController {
    private UserService userService;

    @GetMapping("/{user-id}")
    public ResponseEntity<User> getUserById(@PathVariable("user-id") long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<User> createNewUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.createNewUser(user), HttpStatus.CREATED);
    }

    @PatchMapping("/{user-id}")
    public ResponseEntity<User> updateUserById(@PathVariable("user-id") long id, @RequestBody Map<String, Object> updatedUser) {
        return new ResponseEntity<>(userService.updateUserByFields(id, updatedUser), HttpStatus.OK);
    }

    @DeleteMapping("/{user-id}")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable("user-id") long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
