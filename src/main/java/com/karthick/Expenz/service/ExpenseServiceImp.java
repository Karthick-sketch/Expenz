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
    @Cacheable(value = "expenses:user", key = "#userId")
    public List<Expense> getExpensesByUsedId(long userId) {
        return expenseRepository.findByUserId(userId);
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
            @CacheEvict(value = "expenses:user", key = "#userId"),
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
            @CacheEvict(value = "expenses:user", key = "#userId"),
            @CacheEvict(value = "expense", key = "#id")
    })
    public void deleteExpenseById(long id, long userId) {
        Expense expense = getExpenseById(id, userId);
        expenseRepository.delete(expense);
    }
}
