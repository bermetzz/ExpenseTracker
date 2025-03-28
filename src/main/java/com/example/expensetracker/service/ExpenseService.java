package com.example.expensetracker.service;

import com.example.expensetracker.dto.ExpenseRequest;
import com.example.expensetracker.dto.ExpenseResponse;

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
    BigDecimal getTotalForPeriod(String userEmail, String period);
}
