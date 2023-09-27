package com.karthick.Expenz.service;

import com.karthick.Expenz.common.ApiResponse;
import com.karthick.Expenz.common.RedisCache;
import com.karthick.Expenz.entity.Expense;
import com.karthick.Expenz.exception.BadRequestException;
import com.karthick.Expenz.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private RedisCache redisCache;

    public ApiResponse findAllExpenses() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(expenseRepository.findAll());
        return apiResponse;
    }

    public ApiResponse findExpensesById(long id) {
        ApiResponse apiResponse = new ApiResponse();
        Object cachedData = redisCache.retrieveDateFromCache("expense:" + id);
        if (cachedData != null) {
            apiResponse.setData(cachedData);
            System.out.println("From Cache");
        } else {
            Optional<Expense> expense = expenseRepository.findById(id);
            if (expense.isEmpty()) {
                throw new NoSuchElementException("expecting expense is not found");
            }
            apiResponse.setData(expense.get());
            redisCache.cacheData(("expense:" + id), expense.get());
            System.out.println("From Database");
        }
        return apiResponse;
    }

    public ApiResponse findExpensesByUserId(long userId) {
        ApiResponse apiResponse = new ApiResponse();
        Object cachedData = redisCache.retrieveDateFromCache("user-expenses:" + userId);
        if (cachedData != null) {
            apiResponse.setData(cachedData);
            System.out.println("From Cache");
        } else {
            Object newData = expenseRepository.findByUserId(userId);
            apiResponse.setData(newData);
            redisCache.cacheData(("user-expenses:" + userId), newData);
            System.out.println("From Database");
        }
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

    public ApiResponse updateExpenseById(long id, Map<String, Object> fields) {
        Optional<Expense> expense = expenseRepository.findById(id);
        if (expense.isEmpty()) {
            throw new NoSuchElementException("expecting expense is not found");
        }

        ApiResponse apiResponse = new ApiResponse();
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Expense.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, expense.get(), value);
            }
        });
        apiResponse.setData(expenseRepository.save(expense.get()));
        redisCache.deleteCachedData("expense:" + id);
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
            redisCache.deleteCachedData("expense:" + id);
            apiResponse.setData("expense has been deleted");
        } else {
            throw new BadRequestException("problem with deleting the expense");
        }

        return apiResponse;
    }
}
