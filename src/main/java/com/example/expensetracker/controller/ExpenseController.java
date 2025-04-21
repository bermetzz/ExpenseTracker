package com.example.expensetracker.controller;

import com.example.expensetracker.dto.ExpenseRequest;
import com.example.expensetracker.dto.ExpenseResponse;
import com.example.expensetracker.dto.TotalResponse;
import com.example.expensetracker.entity.Currency;
import com.example.expensetracker.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    @PostMapping
    public ExpenseResponse addExpense(@RequestBody ExpenseRequest request,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        return expenseService.addExpense(userDetails.getUsername(), request);
    }

    @GetMapping
    public List<ExpenseResponse> getAllExpenses(@AuthenticationPrincipal UserDetails userDetails) {
        return expenseService.getAllExpenses(userDetails.getUsername());
    }

    @DeleteMapping("/{id}")
    public void deleteExpense(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        expenseService.deleteExpense(userDetails.getUsername(), id);
    }

    @GetMapping("/filter")
    public List<ExpenseResponse> filter(@RequestParam(required = false) LocalDate from,
                                        @RequestParam(required = false) LocalDate to,
                                        @RequestParam(required = false) Long categoryId,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        return expenseService.filterExpenses(userDetails.getUsername(), from, to, categoryId);
    }

    @GetMapping("/analytics/category")
    public Map<String, BigDecimal> totalByCategory(@AuthenticationPrincipal UserDetails userDetails) {
        return expenseService.getTotalByCategory(userDetails.getUsername());
    }

    @GetMapping("/analytics/period")
    public TotalResponse totalByPeriod(@RequestParam String period,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        return expenseService.getTotalForPeriod(email, period);
    }
}
