package com.example.expensetracker.bot;

import com.example.expensetracker.service.ExpenseApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class ExpenseBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String username;

    @Value("${telegram.bot.token}")
    private String token;

    private final UserSessionService sessionService;
    private final ExpenseApiService apiService;

    public ExpenseBot(UserSessionService sessionService, ExpenseApiService apiService) {
        this.sessionService = sessionService;
        this.apiService = apiService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) return;

        var message = update.getMessage();
        var chatId = message.getChatId();
        var text = message.getText();

        if (text.startsWith("jwt ")) {
            var jwt = text.substring(4).trim();
            sessionService.saveToken(chatId, jwt);
            send(chatId, "✅ JWT is saved.");
            return;
        }

        switch (text) {
            case "/start" -> send(chatId, """
                    👋 Hello! This is the ExpenseTracker bot.
                    Enter the command:
                    `jwt <your token>` — to log in
                    `/month` — monthly expenses
                    `/category` — by category
                    """);

            case "/month" -> {
                if (!sessionService.hasToken(chatId)) {
                    send(chatId, "⚠ First send your JWT: `jwt <token>`");
                } else {
                    var jwt = sessionService.getToken(chatId);
                    var result = apiService.getMonthExpense(jwt);
                    send(chatId, "📆 Expenses per month: " + result);
                }
            }

            case "/category" -> {
                if (!sessionService.hasToken(chatId)) {
                    send(chatId, "⚠ First send your JWT: `jwt <token>`");
                } else {
                    var jwt = sessionService.getToken(chatId);
                    var result = apiService.getCategoryStats(jwt);
                    send(chatId, "📊 By category:\n" + result);
                }
            }

            default -> send(chatId, "Unknown command. Enter /start");
        }
    }

    private void send(Long chatId, String text) {
        var msg = SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .parseMode("Markdown")
                .build();
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
