package com.example.expensetracker.repository;

import com.example.expensetracker.entity.Category;
import com.example.expensetracker.entity.Expense;
import com.example.expensetracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUser(User user);
    List<Expense> findByUserAndDateBetween(User user, LocalDate start, LocalDate end);
    List<Expense> findByUserAndCategory(User user, Category category);
    @Query("SELECT e FROM Expense e WHERE e.user = :user " +
            "AND (:from IS NULL OR e.date >= :from) " +
            "AND (:to IS NULL OR e.date <= :to) " +
            "AND (:categoryId IS NULL OR e.category.id = :categoryId)")
    List<Expense> findAllByUserAndFilters(
            @Param("user") User user,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("categoryId") Long categoryId
    );
}
