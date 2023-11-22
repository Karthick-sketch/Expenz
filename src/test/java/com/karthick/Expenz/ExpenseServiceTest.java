package com.karthick.Expenz;

import com.karthick.Expenz.common.Constants;
import com.karthick.Expenz.entity.Expense;
import com.karthick.Expenz.exception.BadRequestException;
import com.karthick.Expenz.repository.ExpenseRepository;
import com.karthick.Expenz.service.ExpenseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {
    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseService expenseService;

    private Expense getTestExpenseData() {
        Expense expense = new Expense();
        expense.setId(1);
        expense.setAmount(50_000.0);
        expense.setCategory("electronics");
        expense.setIncome(false);
        expense.setTitle("Playstation 5");
        expense.setDescription("Play next generation games");
        expense.setDateAdded(new Date());
        expense.setUserId(1L);
        return expense;
    }

    @Test
    public void testFindExpensesById() {
        Expense mockExpense = getTestExpenseData();
        when(expenseRepository.findById(mockExpense.getId())).thenReturn((Optional.of(mockExpense)));

        Expense validExpense = expenseService.findExpensesById(mockExpense.getId(), mockExpense.getUserId());
        Executable wrongId = () -> expenseService.findExpensesById(2, mockExpense.getUserId());
        Executable wrongUserId = () -> expenseService.findExpensesById(mockExpense.getId(), 2);

        assertEquals(mockExpense, validExpense);
        assertThrows(NoSuchElementException.class, wrongId);
        assertThrows(NoSuchElementException.class, wrongUserId);
    }

    @Test
    public void testGetExpensesByUsedId() {
        Expense mockExpense = getTestExpenseData();
        when(expenseRepository.findByUserId(mockExpense.getUserId())).thenReturn(List.of(mockExpense));

        List<Expense> validExpense = expenseService.getExpensesByUsedId(mockExpense.getUserId());
        Executable notFoundUserId = () -> expenseService.getExpensesByUsedId(Constants.NOT_FOUND);

        assertEquals(mockExpense, validExpense.get(0));
        assertThrows(RuntimeException.class, notFoundUserId);
    }

    @Test
    public void testCreateNewExpense() {
        Expense mockExpense = getTestExpenseData();
        when(expenseRepository.save(mockExpense)).thenReturn(mockExpense);

        Expense expense = expenseService.createNewExpense(mockExpense);

        assertEquals(mockExpense, expense);
        verify(expenseRepository, times(1)).save(mockExpense);
    /*
        # need to clarify how to pass invalid type to primitive types
        Executable invalidExpense = () -> expenseService.createNewExpense(mockExpense);
        assertThrows(BadRequestException.class, invalidExpense);
     */
    }

    @Test
    public void testUpdateExpenseById() {
        Expense mockExpense = getTestExpenseData();
        when(expenseRepository.findById(mockExpense.getId())).thenReturn((Optional.of(mockExpense)));
        when(expenseRepository.save(mockExpense)).thenReturn(mockExpense);

        Map<String, Object> updatedFields = Map.of("amount", 45_000.0);
        Expense validExpense = expenseService.updateExpenseById(mockExpense.getId(), updatedFields, mockExpense.getUserId());
        Executable wrongId = () -> expenseService.updateExpenseById(2, updatedFields, mockExpense.getUserId());
        Executable wrongUserId = () -> expenseService.updateExpenseById(mockExpense.getId(), updatedFields, 2);
        Map<String, Object> invalidTyeField = Map.of("amount", "45_000.0");
        Executable invalidExpense = () -> expenseService.updateExpenseById(mockExpense.getId(), invalidTyeField, mockExpense.getUserId());

        assertEquals(mockExpense, validExpense);
        assertThrows(NoSuchElementException.class, wrongId);
        assertThrows(NoSuchElementException.class, wrongUserId);
        assertThrows(BadRequestException.class, invalidExpense);
        verify(expenseRepository, times(1)).save(mockExpense);
    }

    @Test
    public void testDeleteExpenseById() {
        Expense mockExpense = getTestExpenseData();
        when(expenseRepository.findById(mockExpense.getId())).thenReturn((Optional.of(mockExpense)));

        String deleteMsg = expenseService.deleteExpenseById(mockExpense.getId(), mockExpense.getUserId());
        Executable wrongId = () -> expenseService.deleteExpenseById(2, mockExpense.getUserId());
        Executable wrongUserId = () -> expenseService.deleteExpenseById(mockExpense.getId(), 2);

        assertEquals("expense has been deleted", deleteMsg);
        assertThrows(NoSuchElementException.class, wrongId);
        assertThrows(NoSuchElementException.class, wrongUserId);
        verify(expenseRepository, times(1)).delete(mockExpense);
    }
}