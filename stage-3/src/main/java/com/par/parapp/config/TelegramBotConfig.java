package com.par.parapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.par.parapp.bot.AchievementBot;
import com.par.parapp.config.jwt.JwtUtils;
import com.par.parapp.repository.UserRepository;

@Configuration
public class TelegramBotConfig {
    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.username}")
    private String botUsername;

    @Bean
    public TelegramLongPollingBot bot(JwtUtils jwtUtils, UserRepository userRepository) {
        return new AchievementBot(botToken, botUsername, userRepository);
    }

    @Bean
    public TelegramBotsApi telegramBotsApi(JwtUtils jwtUtils, UserRepository userRepository)
            throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(bot(jwtUtils, userRepository));
        return api;
    }
}