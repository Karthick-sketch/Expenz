package com.karthick.Expenz.service;

import com.karthick.Expenz.common.ApiResponse;
import com.karthick.Expenz.entity.Expense;
import com.karthick.Expenz.exception.BadRequestException;
import com.karthick.Expenz.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    public ApiResponse findExpensesById(long id) {
        ApiResponse apiResponse = new ApiResponse();
        Optional<Expense> expense = expenseRepository.findById(id);
        if (expense.isEmpty()) {
            throw new NoSuchElementException("expecting expense is not found");
        }
        apiResponse.setData(expense.get());
        return apiResponse;
    }

    @Cacheable(value = "expenses:user", key = "#userId")
    public List<Expense> getExpensesByUsedId(long userId) {
        return expenseRepository.findByUserId(userId);
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

    @CacheEvict(value = "expenses:user", key = "#userId")
    public ApiResponse updateExpenseById(long id, Map<String, Object> fields, long userId) {
        Optional<Expense> expense = expenseRepository.findById(id);
        if (expense.isEmpty() || expense.get().getId() != userId) {
            throw new NoSuchElementException("expecting expense is not found");
        }

        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Expense.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, expense.get(), value);
            }
        });

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(expenseRepository.save(expense.get()));
        return apiResponse;
    }

    @CacheEvict(value = "expenses:user", key = "#userId")
    public ApiResponse deleteExpenseById(long id, long userId) {
        Optional<Expense> expense = expenseRepository.findById(id);
        if (expense.isEmpty() || expense.get().getId() != userId) {
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
