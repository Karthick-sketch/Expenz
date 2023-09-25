package com.karthick.Expenz.service;

import com.karthick.Expenz.entity.Expense;
import com.karthick.Expenz.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    public Expense findExpensesById(long id) {
        Optional<Expense> expense = expenseRepository.findById(id);
        return expense.orElse(null);
    }

    public List<Expense> findAllExpenses() {
        return expenseRepository.findAll();
    }

    public Expense createNewExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    public Expense updateExpenseById(long id, Expense updatedExpense) {
        Expense expense = expenseRepository.findById(id).orElse(null);
        if (expense != null) {
            expense.setExpense(updatedExpense);
            return expenseRepository.save(expense);
        }
        return null;
    }

    public boolean deleteExpenseById(long id) {
        Expense expense = expenseRepository.findById(id).orElse(null);
        if (expense != null) {
            expenseRepository.delete(expense);
            return expenseRepository.findById(id).isEmpty();
        }
        return false;
    }
}
