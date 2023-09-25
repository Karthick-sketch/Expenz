package com.karthick.Expenz.controller;

import com.karthick.Expenz.entity.Expense;
import com.karthick.Expenz.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ExpensesController {
    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/expenses")
    public List<Expense> getAllExpenses() {
        return expenseService.findAllExpenses();
    }

    @GetMapping("/expense/{id}")
    public Expense getExpensesById(@PathVariable("id") long id) {
        return expenseService.findExpensesById(id);
    }

    @PostMapping("/expense")
    public Expense createNewExpense(@RequestBody Expense expense) {
        return expenseService.createNewExpense(expense);
    }

    @PatchMapping("/expense/{id}")
    public Expense updateExpenseById(@PathVariable("id") long id, @RequestBody Expense newData) {
        return expenseService.updateExpenseById(id, newData);
    }

    @DeleteMapping("/expense/{id}")
    public boolean deleteExpenseById(@PathVariable("id") long id) {
        return expenseService.deleteExpenseById(id);
    }
}
