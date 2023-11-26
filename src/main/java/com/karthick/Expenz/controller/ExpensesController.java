package com.karthick.Expenz.controller;

import com.karthick.Expenz.entity.Expense;
import com.karthick.Expenz.security.UserSession;
import com.karthick.Expenz.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ExpensesController {
    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private UserSession userSession;

    @GetMapping("/expense")
    public ResponseEntity<List<Expense>> getUserExpenses() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(expenseService.getExpensesByUsedId(userSession.getAuthenticatedUserId()));
    }

    @GetMapping("/expense/{id}")
    public ResponseEntity<Expense> getExpensesById(@PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(expenseService.findExpensesById(id, userSession.getAuthenticatedUserId()));
    }

    @PostMapping("/expense")
    public ResponseEntity<Expense> createNewExpense(@RequestBody Expense expense) {
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseService.createNewExpense(expense));
    }

    @PatchMapping("/expense/{id}")
    public ResponseEntity<Expense> updateExpenseById(@PathVariable("id") long id, @RequestBody Map<String, Object> newData) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(expenseService.updateExpenseById(id, newData, userSession.getAuthenticatedUserId()));
    }

    @DeleteMapping("/expense/{id}")
    public ResponseEntity<String> deleteExpenseById(@PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(expenseService.deleteExpenseById(id, userSession.getAuthenticatedUserId()));
    }
}
