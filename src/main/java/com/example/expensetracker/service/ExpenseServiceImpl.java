package com.example.expensetracker.service;

import com.example.expensetracker.dto.ExpenseRequest;
import com.example.expensetracker.dto.ExpenseResponse;
import com.example.expensetracker.dto.TotalResponse;
import com.example.expensetracker.entity.Category;
import com.example.expensetracker.entity.Currency;
import com.example.expensetracker.entity.Expense;
import com.example.expensetracker.entity.User;
import com.example.expensetracker.repository.CategoryRepository;
import com.example.expensetracker.repository.ExpenseRepository;
import com.example.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ExpenseResponse addExpense(String userEmail, ExpenseRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Expense expense = Expense.builder()
                .description(request.description())
                .amount(request.amount())
                .date(request.date())
                .user(user)
                .category(category)
                .currency(request.currency() != null ? request.currency() : Currency.KGS)
                .build();
        Expense saved = expenseRepository.save(expense);

        return mapToResponse(saved);
    }

    private ExpenseResponse mapToResponse(Expense expense) {
        return new ExpenseResponse(
                expense.getId(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getDate(),
                expense.getCategory().getName()
        );
    }

    @Override
    public List<ExpenseResponse> getAllExpenses(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return expenseRepository.findByUser(user).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteExpense(String userEmail, Long expenseId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }
        expenseRepository.delete(expense);
    }

    @Override
    public List<ExpenseResponse> filterExpenses(String userEmail, LocalDate from, LocalDate to, Long categoryId) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        List<Expense> expenses = expenseRepository.findByUser(user);

        return expenses.stream()
                .filter(e -> from == null || !e.getDate().isBefore(from))
                .filter(e -> to == null || !e.getDate().isAfter(to))
                .filter(e -> categoryId == null || e.getCategory().getId().equals(categoryId))
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public Map<String, BigDecimal> getTotalByCategory(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        return expenseRepository.findByUser(user).stream()
                .collect(Collectors.groupingBy(
                        e -> e.getCategory().getName() + " (" + e.getCurrency().name() + ")",
                        Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)
                ));

    }

    @Override
    public TotalResponse getTotalForPeriod(String userEmail, String period) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        Currency userCurrency = getUserCurrency(userEmail);
        LocalDate now = LocalDate.now();
        LocalDate start;

        switch (period.toLowerCase()) {
            case "day" -> start = now;
            case "week" -> start = now.minusDays(6);
            case "month" -> start = now.withDayOfMonth(1);
            default -> throw new IllegalArgumentException("Invalid period: " + period);
        }

        BigDecimal totalAmount = sumExpensesInUserCurrency(user, start, now, userCurrency);
        Map<Currency, BigDecimal> amountMap = Map.of(userCurrency, totalAmount);
        return new TotalResponse(amountMap, userCurrency);
    }

    private BigDecimal sumByDateRange(User user, LocalDate from, LocalDate to) {
        return expenseRepository.findByUserAndDateBetween(user, from, to).stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Currency getUserCurrency(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .map(User::getCurrency)
                .orElse(Currency.KGS);
    }

    private BigDecimal sumExpensesInUserCurrency(User user, LocalDate from, LocalDate to, Currency targetCurrency) {
        Map<Currency, BigDecimal> exchangeRates = Map.of(
                Currency.KGS, BigDecimal.valueOf(1),
                Currency.EUR, BigDecimal.valueOf(95),
                Currency.USD, BigDecimal.valueOf(87)
        );

        return expenseRepository.findByUserAndDateBetween(user, from, to).stream()
                .map(expense -> {
                    BigDecimal rate = exchangeRates.getOrDefault(expense.getCurrency(), BigDecimal.ONE)
                            .divide(exchangeRates.getOrDefault(targetCurrency, BigDecimal.ONE).round(new MathContext(2)));
                    return expense.getAmount().multiply(rate);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
