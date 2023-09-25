package com.karthick.Expenz.service;

import com.karthick.Expenz.common.ApiResponse;
import com.karthick.Expenz.entity.Expense;
import com.karthick.Expenz.exception.BadRequestException;
import com.karthick.Expenz.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    public ApiResponse findAllExpenses() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(expenseRepository.findAll());
        return apiResponse;
    }

    public ApiResponse findExpensesById(long id) {
        ApiResponse apiResponse = new ApiResponse();
        Optional<Expense> expense = expenseRepository.findById(id);
        if (expense.isEmpty()) {
            throw new NoSuchElementException("expecting expense is not found");
        }
        apiResponse.setData(expense);
        return apiResponse;
    }

    public ApiResponse findExpensesByUserId(long userId) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(expenseRepository.findByUserId(userId));
        return apiResponse;
    }

    public ApiResponse createNewExpense(Expense expense) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            apiResponse.setData(expenseRepository.save(expense));
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
        return apiResponse;
    }

    public ApiResponse updateExpenseById(long id, Expense updatedExpense) {
        Optional<Expense> expense = expenseRepository.findById(id);
        if (expense.isEmpty()) {
            throw new NoSuchElementException("expecting expense is not found");
        }
        Expense expense1 = expense.get();
        expense1.setExpense(updatedExpense);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(expenseRepository.save(expense1));
        return apiResponse;
    }

    public ApiResponse deleteExpenseById(long id) {
        Optional<Expense> expense = expenseRepository.findById(id);
        if (expense.isEmpty()) {
            throw new NoSuchElementException("expecting expense is not found");
        }

        expenseRepository.delete(expense.get());
        ApiResponse apiResponse = new ApiResponse();
        if (expenseRepository.findById(id).isEmpty()) {
            apiResponse.setData("expense has been deleted");
        } else {
            throw new BadRequestException("problem with deleting the expense");
        }

        return apiResponse;
    }
}
