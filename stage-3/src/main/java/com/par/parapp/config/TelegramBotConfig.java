package com.par.parapp.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.par.parapp.bot.AchievementBot;

@Configuration
public class TelegramBotConfig {
    @Value("${bot.token}")
    private String botToken;
    
    @Value("${bot.username}")
    private String botUsername;
    
    @Bean
    public TelegramLongPollingBot Bot() {
        return new AchievementBot(botToken, botUsername);
    }
    
    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(Bot());
        return api;
    }
}