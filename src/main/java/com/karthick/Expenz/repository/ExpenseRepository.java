package com.karthick.Expenz.repository;

import com.karthick.Expenz.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query(value = "SELECT * FROM expenz.expenses WHERE MONTH(date_added) = ? AND YEAR(date_added) = ? AND user_id = ?", nativeQuery = true)
    List<Expense> findExpensesByMonthAndYear(int month, int year, long userId);

    @Query(value = "SELECT * FROM expenz.expenses WHERE is_it_income = ? AND MONTH(date_added) = ? AND YEAR(date_added) = ? AND user_id = ?", nativeQuery = true)
    List<Expense> findExpensesByTypeMonthAndYear(boolean isItIncome, int month, int year, long userId);
}
