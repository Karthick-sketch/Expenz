package com.karthick.Expenz.repository;

import com.karthick.Expenz.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
