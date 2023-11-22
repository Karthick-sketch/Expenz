package com.karthick.Expenz.controller;

import com.karthick.Expenz.common.ApiResponse;
import com.karthick.Expenz.entity.Expense;
import com.karthick.Expenz.security.UserSession;
import com.karthick.Expenz.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ExpensesController {
    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private UserSession userSession;

    @GetMapping("/expense")
    public ResponseEntity<ApiResponse> getUserExpenses() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(expenseService.getExpensesByUsedId(userSession.getAuthenticatedUserId()));
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @GetMapping("/expense/{id}")
    public ResponseEntity<ApiResponse> getExpensesById(@PathVariable("id") long id) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(expenseService.findExpensesById(id, userSession.getAuthenticatedUserId()));
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PostMapping("/expense")
    public ResponseEntity<ApiResponse> createNewExpense(@RequestBody Expense expense) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(expenseService.createNewExpense(expense));
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PatchMapping("/expense/{id}")
    public ResponseEntity<ApiResponse> updateExpenseById(@PathVariable("id") long id, @RequestBody Map<String, Object> newData) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(expenseService.updateExpenseById(id, newData, userSession.getAuthenticatedUserId()));
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @DeleteMapping("/expense/{id}")
    public ResponseEntity<ApiResponse> deleteExpenseById(@PathVariable("id") long id) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(expenseService.deleteExpenseById(id, userSession.getAuthenticatedUserId()));
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
