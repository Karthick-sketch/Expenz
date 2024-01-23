package com.karthick.Expenz;

import com.karthick.Expenz.entity.Expense;
import com.karthick.Expenz.entity.User;
import com.karthick.Expenz.exception.BadRequestException;
import com.karthick.Expenz.exception.EntityNotFoundException;
import com.karthick.Expenz.repository.ExpenseRepository;
import com.karthick.Expenz.service.ExpenseServiceImp;
import com.karthick.Expenz.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {
    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ExpenseServiceImp expenseService;

    private final Date date = new Date();

    private Expense getTestExpenseData() {
        Expense expense = new Expense();
        expense.setId(1);
        expense.setAmount(50_000.0);
        expense.setCategory("electronics");
        expense.setItIncome(false);
        expense.setTitle("Playstation 5");
        expense.setDescription("Play next generation games");
        expense.setDateAdded(date);

        User user = new User();
        user.setId(1);
        user.setUsername("Kang");
        user.setEmail("kang@marvel.com");
        user.setPassword("conqueror");
        expense.setUser(user);

        return expense;
    }

    @Test
    public void testFindExpensesById() {
        Expense mockExpense = getTestExpenseData();
        when(expenseRepository.findById(mockExpense.getId())).thenReturn((Optional.of(mockExpense)));

        Expense validExpense = expenseService.getExpenseById(mockExpense.getId(), mockExpense.getUser().getId());
        Executable wrongId = () -> expenseService.getExpenseById(2, mockExpense.getUser().getId());
        Executable wrongUserId = () -> expenseService.getExpenseById(mockExpense.getId(), 2);

        assertEquals(mockExpense, validExpense);
        assertThrows(EntityNotFoundException.class, wrongId);
        assertThrows(EntityNotFoundException.class, wrongUserId);
    }

    @Test
    public void testFetchExpensesByMonthAndYear() {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int month = localDate.getMonthValue(), year = localDate.getYear();

        Expense mockExpense = getTestExpenseData();
        when(expenseRepository.findExpensesByMonthAndYear(month, year, mockExpense.getUser().getId())).thenReturn(List.of(mockExpense));

        List<Expense> validExpense = expenseService.fetchExpensesByMonthAndYear(month, year, mockExpense.getUser().getId());
        assertEquals(mockExpense, validExpense.get(0));

        // not working, will fix it
        // Executable notFoundUserId = () -> expenseService.fetchExpensesByMonthAndYear(month, year, SecurityConstants.NOT_FOUND);
        // assertThrows(RuntimeException.class, notFoundUserId);
    }

    @Test
    public void testFetchExpensesByTypeMonthAndYear() {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int month = localDate.getMonthValue(), year = localDate.getYear();

        Expense mockExpense = getTestExpenseData();
        when(expenseRepository.findExpensesByTypeMonthAndYear(mockExpense.isItIncome(), month, year, mockExpense.getUser().getId())).thenReturn(List.of(mockExpense));

        List<Expense> validExpenses = expenseService.fetchExpensesByTypeMonthAndYear(false, month, year, mockExpense.getUser().getId());
        List<Expense> validIncomes = expenseService.fetchExpensesByTypeMonthAndYear(true, month, year, mockExpense.getUser().getId());
        assertEquals(mockExpense, validExpenses.get(0));
        assertTrue(validIncomes.isEmpty());

        // not working, will fix it
        // Executable notFoundUserId = () -> expenseService.fetchExpensesByMonthAndYear(month, year, SecurityConstants.NOT_FOUND);
        // assertThrows(RuntimeException.class, notFoundUserId);
    }

    @Test
    public void testCreateNewExpense() {
        Expense mockExpense = getTestExpenseData();
        when(expenseRepository.save(mockExpense)).thenReturn(mockExpense);

        Expense expense = expenseService.createNewExpense(mockExpense, mockExpense.getUser().getId());

        assertEquals(mockExpense, expense);
        verify(expenseRepository, times(1)).save(mockExpense);
        /*
         * # need to clarify how to pass invalid type to primitive types
         * Executable invalidExpense = () ->
         * expenseService.createNewExpense(mockExpense);
         * assertThrows(BadRequestException.class, invalidExpense);
         */
    }

    @Test
    public void testUpdateExpenseById() {
        Expense mockExpense = getTestExpenseData();
        when(expenseRepository.findById(mockExpense.getId())).thenReturn((Optional.of(mockExpense)));
        when(expenseRepository.save(mockExpense)).thenReturn(mockExpense);

        Map<String, Object> updatedFields = Map.of("amount", 45_000.0);
        Expense validExpense = expenseService.updateExpenseById(mockExpense.getId(), updatedFields,
                mockExpense.getUser().getId());
        Executable wrongId = () -> expenseService.updateExpenseById(2, updatedFields, mockExpense.getUser().getId());
        Executable wrongUserId = () -> expenseService.updateExpenseById(mockExpense.getId(), updatedFields, 2);

        Map<String, Object> invalidFieldType = Map.of("amount", "45_000.0");
        Executable invalidExpense = () -> expenseService.updateExpenseById(mockExpense.getId(), invalidFieldType,
                mockExpense.getUser().getId());

        assertEquals(mockExpense, validExpense);
        assertThrows(EntityNotFoundException.class, wrongId);
        assertThrows(EntityNotFoundException.class, wrongUserId);
        assertThrows(BadRequestException.class, invalidExpense);
        verify(expenseRepository, times(1)).save(mockExpense);
    }

    @Test
    public void testDeleteExpenseById() {
        Expense mockExpense = getTestExpenseData();
        when(expenseRepository.findById(mockExpense.getId())).thenReturn((Optional.of(mockExpense)));

        expenseService.deleteExpenseById(mockExpense.getId(), mockExpense.getUser().getId());
        Executable wrongId = () -> expenseService.deleteExpenseById(2, mockExpense.getUser().getId());
        Executable wrongUserId = () -> expenseService.deleteExpenseById(mockExpense.getId(), 2);

        assertThrows(EntityNotFoundException.class, wrongId);
        assertThrows(EntityNotFoundException.class, wrongUserId);
        verify(expenseRepository, times(1)).delete(mockExpense);
    }
}
