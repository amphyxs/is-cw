package com.par.parapp.dto;

import lombok.Data;

@Data
public class AchievementRequest {
    private Long userId;
    private String achievementType;
}