package com.karthick.Expenz.service;

import com.karthick.Expenz.common.Constants;
import com.karthick.Expenz.entity.Expense;
import com.karthick.Expenz.exception.BadRequestException;
import com.karthick.Expenz.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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

    @Cacheable(value = "expense", key = "#id")
    public Expense findExpensesById(long id, long userId) {
        Optional<Expense> expense = expenseRepository.findById(id);
        if (expense.isEmpty() || expense.get().getUserId() != userId) {
            throw new NoSuchElementException("expecting expense is not found");
        }
        return expense.get();
    }

    @Cacheable(value = "expenses:user", key = "#userId")
    public List<Expense> getExpensesByUsedId(long userId) {
        if (userId == Constants.NOT_FOUND) {
            throw new RuntimeException("something wrong at authentication");
        }
        return expenseRepository.findByUserId(userId);
    }

    public Expense createNewExpense(Expense expense) {
        try {
            return expenseRepository.save(expense);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }

    @Caching(evict = {
        @CacheEvict(value = "expenses:user", key = "#userId"),
        @CacheEvict(value = "expense", key = "#id")
    })
    public Expense updateExpenseById(long id, Map<String, Object> fields, long userId) {
        Optional<Expense> expense = expenseRepository.findById(id);
        if (expense.isEmpty() || expense.get().getUserId() != userId) {
            throw new NoSuchElementException("expecting expense is not found");
        }

        try {
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Expense.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, expense.get(), value);
                }
            });
            return expenseRepository.save(expense.get());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }

    @Caching(evict = {
        @CacheEvict(value = "expenses:user", key = "#userId"),
        @CacheEvict(value = "expense", key = "#id")
    })
    public String deleteExpenseById(long id, long userId) {
        Optional<Expense> expense = expenseRepository.findById(id);
        if (expense.isEmpty() || expense.get().getUserId() != userId) {
            throw new NoSuchElementException("expecting expense is not found");
        }
        expenseRepository.delete(expense.get());
        return "expense has been deleted";
    }
}
