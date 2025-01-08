package com.par.parapp.dto;

import lombok.Data;

@Data
public class AchievementRequest {
    private String userId;
    private String achievementType;
}