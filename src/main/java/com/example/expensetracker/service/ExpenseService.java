package com.example.expensetracker.service;

import com.example.expensetracker.dto.ExpenseRequest;
import com.example.expensetracker.dto.ExpenseResponse;
import com.example.expensetracker.dto.TotalResponse;
import com.example.expensetracker.entity.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ExpenseService {
    ExpenseResponse addExpense(String userEmail, ExpenseRequest request);
    List<ExpenseResponse> getAllExpenses(String userEmail);
    void deleteExpense(String userEmail, Long expenseId);
    List<ExpenseResponse> filterExpenses(String userEmail, LocalDate from, LocalDate to, Long categoryId);
    Map<String, BigDecimal> getTotalByCategory(String userEmail);
    TotalResponse getTotalForPeriod(String userEmail, String period);

    Currency getUserCurrency(String userEmail);
}
