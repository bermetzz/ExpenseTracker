package com.example.expensetracker.dto;

import com.example.expensetracker.entity.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseRequest (
        String description,
        BigDecimal amount,
        LocalDate date,
        Long categoryId,
        Currency currency

) {
}
