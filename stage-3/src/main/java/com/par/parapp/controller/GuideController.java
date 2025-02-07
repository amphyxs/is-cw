package com.par.parapp.controller;

import com.par.parapp.dto.GuideRequest;
import com.par.parapp.model.Game;
import com.par.parapp.model.User;
import com.par.parapp.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/guide")
@CrossOrigin(origins = "*", maxAge = 3600)
public class GuideController {

    private final GameService gameService;

    private final UserService userService;

    private final GuideService guideService;
    private final AuthService authService;

    public GuideController(GameService gameService, UserService userService, GuideService guideService,
            AuthService authService) {
        this.gameService = gameService;
        this.userService = userService;
        this.guideService = guideService;
        this.authService = authService;
    }

    @GetMapping()
    public ResponseEntity<Object> getGuidesBySelectedGame(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String selectedGame) {

        return ResponseEntity.ok(guideService.getGuidesByCondition(selectedGame, page, size));
    }

    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> addGuide(@Valid @RequestBody GuideRequest guideRequest,
            HttpServletRequest httpServletRequest) {
        String login = authService.getLoginFromToken(httpServletRequest);
        User user = userService.getUserByLogin(login);
        Game game = gameService.getGameByName(guideRequest.getGameName());
        guideService.saveGuide(user, game, guideRequest.getGuideText());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
