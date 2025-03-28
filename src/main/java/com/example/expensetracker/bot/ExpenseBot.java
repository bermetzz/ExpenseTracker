package com.example.expensetracker.telegram;

import com.example.expensetracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class ExpenseBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Autowired
    private ExpenseService expenseService;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            String chatId = message.getChatId().toString();
            String text = message.getText();

            switch (text) {
                case "/start" -> sendMsg(chatId, "Привет! Введи свой JWT токен.");
                case "/month" -> {
                    sendMsg(chatId, "За этот месяц: 450₽");
                }
                case "/category" -> {
                    sendMsg(chatId, "Еда: 300₽\nОдежда: 150₽");
                }
                default -> sendMsg(chatId, "Неизвестная команда.");
            }
        }
    }

    private void sendMsg(String chatId, String text) {
        SendMessage msg = new SendMessage(chatId, text);
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
