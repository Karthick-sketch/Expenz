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

@Service
public class ExpenseServiceImp implements ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserService userService;

    @Override
    @Cacheable(value = "expense", key = "#id")
    public Expense findExpensesById(long id, long userId) {
        Expense expense = expenseRepository.findById(id).orElseThrow();
        if (expense.getUser().getId() != userId) {
            throw new NoSuchElementException("expecting expense is not found");
        }
        return expense;
    }

    @Override
    @Cacheable(value = "expenses:user", key = "#userId")
    public List<Expense> getExpensesByUsedId(long userId) {
        if (userId == Constants.NOT_FOUND) {
            throw new BadRequestException("something wrong at authentication");
        }
        return expenseRepository.findByUserId(userId);
    }

    @Override
    public Expense createNewExpense(Expense expense, long userId) {
        if (userId == Constants.NOT_FOUND) {
            throw new BadRequestException("something wrong at authentication");
        }
        try {
            expense.setUser(userService.findUserById(userId));
            return expenseRepository.save(expense);
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "expenses:user", key = "#userId"),
            @CacheEvict(value = "expense", key = "#id")
    })
    public Expense updateExpenseById(long id, Map<String, Object> fields, long userId) {
        Expense expense = expenseRepository.findById(id).orElseThrow();
        if (expense.getUser().getId() != userId) {
            throw new NoSuchElementException("expecting expense is not found");
        }

        try {
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Expense.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, expense, value);
                }
            });
            return expenseRepository.save(expense);
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "expenses:user", key = "#userId"),
            @CacheEvict(value = "expense", key = "#id")
    })
    public void deleteExpenseById(long id, long userId) {
        Expense expense = expenseRepository.findById(id).orElseThrow();
        if (expense.getUser().getId() != userId) {
            throw new NoSuchElementException("expecting expense is not found");
        }
        expenseRepository.delete(expense);
    }
}
