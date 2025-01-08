package com.par.parapp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.par.parapp.dto.AchievementRequest;
import com.par.parapp.service.AchievementService;
import com.par.parapp.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/achievement")
public class AchievementController {

    private final AchievementService achievementService;
    private final AuthService authService;

    @GetMapping()
    public ResponseEntity<List<String>> getUserReceivedAchivements(HttpServletRequest request) {
        String userLogin = authService.getLoginFromToken(request);

        return ResponseEntity.ok(achievementService.getAllReceivedAchievement(userLogin));
    }

    @PostMapping()
    public ResponseEntity<Object> processAchievement(@RequestBody AchievementRequest request) {
        achievementService.processAchievement(request);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}