package com.example.expensetracker.service;


import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ExpenseApiService {

    private final WebClient client = WebClient.create("http://localhost:8080");

    public String getMonthExpense(String token) {
        return client.get()
                .uri("/api/expenses/analytics/period?period=month")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorReturn("API request error")
                .block();
    }

    public String getCategoryStats(String token) {
        return client.get()
                .uri("/api/expenses/analytics/category")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorReturn("API request error")
                .block();
    }
}

