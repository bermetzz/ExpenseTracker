package com.example.expensetracker.dto;

import com.example.expensetracker.entity.Currency;
import java.math.BigDecimal;
import java.util.Map;

public record TotalResponse(
        Map<Currency, BigDecimal> amount,
        Currency currency
) {
}
