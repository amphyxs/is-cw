package com.par.parapp.controller;

import com.par.parapp.dto.AchievementRequest;
import com.par.parapp.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/achievement")
public class AchievementController {
    
    private final AchievementService achievementService;
    
    @PostMapping()
    public void processAchievement(@RequestBody AchievementRequest request) {
        achievementService.processAchievement(request);
    }
}