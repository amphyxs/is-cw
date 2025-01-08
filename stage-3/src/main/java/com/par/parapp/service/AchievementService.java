package com.par.parapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.par.parapp.bot.AchievementBot;
import com.par.parapp.dto.AchievementRequest;
import com.par.parapp.model.Achievement;
import com.par.parapp.repository.AchievementRepository;
import com.par.parapp.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AchievementService {
    private final AchievementBot achievementBot;
    private final AchievementRepository achievementRepository;
    private final UserRepository userRepository;

    public List<String> getAllReceivedAchievement(String receiverLogin) {
        return achievementRepository.getAchievementsByUser(receiverLogin).stream()
                .map(Achievement::getName).toList();
    }

    public void processAchievement(AchievementRequest request) {
        var achievement = new Achievement(request.getAchievementType(),
                userRepository.findById(request.getUserId()).get());
        achievementRepository.save(achievement);

        Long telegramId = userRepository.findTelegramIdByUserId(request.getUserId());

        if (telegramId != null) {
            achievementBot.sendAchievementNotification(
                    telegramId,
                    "Achievement: " + request.getAchievementType());
        }
    }
}