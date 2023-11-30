package com.karthick.Expenz.service;

import com.karthick.Expenz.entity.Expense;

import java.util.List;
import java.util.Map;

public interface ExpenseService {
    Expense findExpensesById(long id, long userId);

    List<Expense> getExpensesByUsedId(long userId);

    Expense createNewExpense(Expense expense, long userId);

    Expense updateExpenseById(long id, Map<String, Object> fields, long userId);

    void deleteExpenseById(long id, long userId);
}
