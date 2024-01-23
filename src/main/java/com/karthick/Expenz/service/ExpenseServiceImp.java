package com.karthick.Expenz.service;

import com.karthick.Expenz.entity.Expense;
import com.karthick.Expenz.exception.BadRequestException;
import com.karthick.Expenz.exception.EntityNotFoundException;
import com.karthick.Expenz.repository.ExpenseRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ExpenseServiceImp implements ExpenseService {
    private ExpenseRepository expenseRepository;
    private UserService userService;

    @Override
    @Cacheable(value = "expense", key = "#id")
    public Expense getExpenseById(long id, long userId) {
        Optional<Expense> expense = expenseRepository.findById(id);
        if (expense.isEmpty() || expense.get().getUser().getId() != userId) {
            throw new EntityNotFoundException(id, Expense.class);
        }
        return expense.get();
    }

    @Override
    @Cacheable(value = "expenses:user-month-year", key = "{#userId, #month, #year}")
    public List<Expense> fetchExpensesByMonthAndYear(int month, int year, long userId) {
        return expenseRepository.findExpensesByMonthAndYear(month, year, userId);
    }

    @Override
    @Cacheable(value = "expenses:user-type-month-year", key = "{#userId, #isItIncome, #month, #year}")
    public List<Expense> fetchExpensesByTypeMonthAndYear(boolean isItIncome, int month, int year, long userId) {
        return expenseRepository.findExpensesByTypeMonthAndYear(isItIncome, month, year, userId);
    }

    @Override
    public Expense createNewExpense(Expense expense, long userId) {
        try {
            expense.setUser(userService.getUserById(userId));
            return expenseRepository.save(expense);
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "expenses:user-month-year", key = "{#userId, '*'}"), // not working, will fix it
            @CacheEvict(value = "expenses:user-type-month-year", key = "{#userId, '*'}"), // not working, will fix it
            @CacheEvict(value = "expense", key = "#id")
    })
    public Expense updateExpenseById(long id, Map<String, Object> fields, long userId) {
        Expense expense = getExpenseById(id, userId);
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
            @CacheEvict(value = "expenses:user-month-year", key = "{#userId, '*'}"), // not working, will fix it
            @CacheEvict(value = "expenses:user-type-month-year", key = "{#userId, '*'}"), // not working, will fix it
            @CacheEvict(value = "expense", key = "#id")
    })
    public void deleteExpenseById(long id, long userId) {
        expenseRepository.delete(getExpenseById(id, userId));
    }
}
