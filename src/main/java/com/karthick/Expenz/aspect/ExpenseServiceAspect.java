package com.karthick.Expenz.aspect;

import com.karthick.Expenz.exception.BadRequestException;
import com.karthick.Expenz.security.SecurityConstants;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExpenseServiceAspect {
    @Before("execution(* com.karthick.Expenz.service.ExpenseService.getExpensesByUsedId(long))")
    public void verifyUserIdInGetExpensesByUsedIdMethod(JoinPoint joinPoint) {
        validateUserId((long) joinPoint.getArgs()[0]);
        System.out.println("Before advice from Expense.getExpensesByUsedId(long) method");
    }

    @Before("execution(* com.karthick.Expenz.service.ExpenseService.createNewExpense(..))")
    public void verifyUserIdInCreateNewExpenseMethod(JoinPoint joinPoint) {
        validateUserId((long) joinPoint.getArgs()[1]);
        System.out.println("Before advice from Expense.createNewExpense(Expense, long) method");
    }

    private void validateUserId(long userId) {
        if (userId == SecurityConstants.NOT_FOUND) {
            throw new BadRequestException("something wrong at authentication");
        }
    }
}
