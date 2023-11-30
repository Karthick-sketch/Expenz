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
@RequestMapping("/expense")
public class ExpensesController {
    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private UserSession userSession;

    @GetMapping("/all")
    public ResponseEntity<List<Expense>> getUserExpenses() {
        return new ResponseEntity<>(expenseService.getExpensesByUsedId(userSession.getAuthenticatedUserId()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpensesById(@PathVariable("id") long id) {
        return new ResponseEntity<>(expenseService.findExpensesById(id, userSession.getAuthenticatedUserId()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Expense> createNewExpense(@RequestBody Expense expense) {
        return new ResponseEntity<>(expenseService.createNewExpense(expense, userSession.getAuthenticatedUserId()), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Expense> updateExpenseById(@PathVariable("id") long id, @RequestBody Map<String, Object> newData) {
        return new ResponseEntity<>(expenseService.updateExpenseById(id, newData, userSession.getAuthenticatedUserId()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteExpenseById(@PathVariable("id") long id) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
