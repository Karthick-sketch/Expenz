package com.karthick.Expenz.controller;

import com.karthick.Expenz.common.ApiResponse;
import com.karthick.Expenz.entity.Expense;
import com.karthick.Expenz.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ExpensesController {
    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/expense")
    public ResponseEntity<ApiResponse> getUserExpenses() {
        ApiResponse apiResponse = expenseService.getExpenses();
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @GetMapping("/expense/{id}")
    public ResponseEntity<ApiResponse> getExpensesById(@PathVariable("id") long id) {
        ApiResponse apiResponse = expenseService.findExpensesById(id);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PostMapping("/expense")
    public ResponseEntity<ApiResponse> createNewExpense(@RequestBody Expense expense) {
        ApiResponse apiResponse = expenseService.createNewExpense(expense);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PatchMapping("/expense/{id}")
    public ResponseEntity<ApiResponse> updateExpenseById(@PathVariable("id") long id, @RequestBody Map<String, Object> newData) {
        ApiResponse apiResponse = expenseService.updateExpenseById(id, newData);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @DeleteMapping("/expense/{id}")
    public ResponseEntity<ApiResponse> deleteExpenseById(@PathVariable("id") long id) {
        ApiResponse apiResponse = expenseService.deleteExpenseById(id);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
