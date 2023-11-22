package com.karthick.Expenz.controller;

import com.karthick.Expenz.common.ApiResponse;
import com.karthick.Expenz.entity.User;
import com.karthick.Expenz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UsersController {
    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public ResponseEntity<ApiResponse> getAllUsers() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(userService.findAllUsers());
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @GetMapping("/user/{user-id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable("user-id") long id) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(userService.findUserById(id));
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PostMapping("/user")
    public ResponseEntity<ApiResponse> createNewUser(@RequestBody User user) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(userService.createNewUser(user));
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PatchMapping("/user/{user-id}")
    public ResponseEntity<ApiResponse> updateUserById(@PathVariable("user-id") long id, @RequestBody Map<String, Object> updatedUser) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(userService.updateUserByFields(id, updatedUser));
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @DeleteMapping("/user/{user-id}")
    public ResponseEntity<ApiResponse> deleteUserById(@PathVariable("user-id") long id) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(userService.deleteUserById(id));
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
