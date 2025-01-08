package com.par.parapp.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.par.parapp.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AchievementBot extends TelegramLongPollingBot {
    private final String botToken;
    private final String botUsername;
    private final UserRepository userRepository;

    public AchievementBot(
            @Value("${bot.token}") String botToken,
            @Value("${bot.username}") String botUsername,
            UserRepository userRepository) {
        this.botToken = botToken;
        this.botUsername = botUsername;
        this.userRepository = userRepository;
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
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if (messageText.startsWith("/start ")) {
                try {
                    String login = messageText.substring(7);
                    userRepository.findByLogin(login).ifPresent(user -> {
                        user.setTelegramId(chatId);
                        userRepository.save(user);
                        sendMessage(chatId, "Successfully linked Telegram account to user: " + login);
                    });
                } catch (Exception e) {
                    sendMessage(chatId, "Error processing token");
                }
            }
        }
    }

    public void sendAchievementNotification(Long chatId, String achievement) {
        sendMessage(chatId, "🏆 Congratulations! You've earned: " + achievement);
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}