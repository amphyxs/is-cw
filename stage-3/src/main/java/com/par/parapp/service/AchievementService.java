package com.par.parapp.service;

import com.par.parapp.bot.AchievementBot;
import com.par.parapp.dto.AchievementRequest;
import com.par.parapp.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AchievementService {
    
    private final AchievementBot achievementBot;
    private final UserRepository userRepository;

    public void processAchievement(AchievementRequest request) {
        Long telegramId = userRepository.findTelegramIdByUserId(request.getUserId());

        if (telegramId != null) {
            achievementBot.sendAchievementNotification(
                telegramId,
                "Achievement: " + request.getAchievementType()
            );
        }
    }
}