package com.karthick.Expenz.controller;

import com.karthick.Expenz.entity.Expense;
import com.karthick.Expenz.security.UserSession;
import com.karthick.Expenz.service.ExpenseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/expenz")
@AllArgsConstructor
public class ExpensesController {
    private ExpenseService expenseService;
    private UserSession userSession;

    @GetMapping("/all")
    public ResponseEntity<List<Expense>> getAllExpenses() {
        return new ResponseEntity<>(expenseService.fetchAllExpensesByUserId(userSession.getAuthenticatedUserId()), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpensesByMonthAndYear(@RequestParam int month, int year) {
        return new ResponseEntity<>(expenseService.fetchExpensesByMonthAndYear(month, year, userSession.getAuthenticatedUserId()), HttpStatus.OK);
    }

    @GetMapping("/expenses")
    public ResponseEntity<List<Expense>> getExpensesByMonthAndYear(@RequestParam int month, int year) {
        return new ResponseEntity<>(expenseService.fetchExpensesByTypeMonthAndYear(false, month, year, userSession.getAuthenticatedUserId()), HttpStatus.OK);
    }

    @GetMapping("/incomes")
    public ResponseEntity<List<Expense>> getIncomesByMonthAndYear(@RequestParam int month, int year) {
        return new ResponseEntity<>(expenseService.fetchExpensesByTypeMonthAndYear(true, month, year, userSession.getAuthenticatedUserId()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpensesById(@PathVariable("id") long id) {
        return new ResponseEntity<>(expenseService.getExpenseById(id, userSession.getAuthenticatedUserId()), HttpStatus.OK);
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
        expenseService.deleteExpenseById(id, userSession.getAuthenticatedUserId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
