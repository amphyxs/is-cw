package com.par.parapp.bot;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.springframework.beans.factory.annotation.Value;

@Component
@Slf4j
public class AchievementBot extends TelegramLongPollingBot {
    private final String botToken;
    private final String botUsername;

    public AchievementBot(@Value("${bot.token}") String botToken, 
                          @Value("${bot.username}") String botUsername) {
        this.botToken = botToken;
        this.botUsername = botUsername;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        // Handle incoming messages if needed
    }

    public void sendAchievementNotification(Long chatId, String achievement) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("🏆 Congratulations! You've earned: " + achievement);
        
        try {
            execute(message);
        } catch (TelegramApiException e) {
            //log.error("Error sending achievement notification", e);
        }
    }
}