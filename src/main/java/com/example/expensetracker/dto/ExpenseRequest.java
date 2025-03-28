package com.example.expensetracker.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseRequest (
        String description,
        BigDecimal amount,
        LocalDate date,
        Long categoryId
) {
}
