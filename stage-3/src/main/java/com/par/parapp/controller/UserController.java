package com.par.parapp.controller;

import com.par.parapp.dto.*;
import com.par.parapp.service.AuthService;
import com.par.parapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final AuthService authService;
    private final UserService userService;

    public UserController(AuthService authService, UserService userService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PreAuthorize("hasAnyRole('USER','DEV')")
    @GetMapping()
    public ResponseEntity<Object> getUserLogin(HttpServletRequest httpServletRequest) {
        String login = authService.getLoginFromToken(httpServletRequest);
        return ResponseEntity.ok(new MessageResponse(login));
    }

    @GetMapping("/exist/{login}")
    public ResponseEntity<Object> checkUserLogin(@PathVariable String login) {
        if (userService.checkUserOnExist(login))
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/status-dates/{login}")
    public ResponseEntity<Object> getStatusAndDates(@PathVariable String login) {
        return ResponseEntity.ok(userService.getUserLastLoginDate(login));
    }

    @PreAuthorize("hasAnyRole('USER','DEV')")
    @PatchMapping("/logout/{login}")
    public ResponseEntity<Object> logoutFromUser(@PathVariable String login) {
        userService.logoutAsUser(login);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/balance-add")
    public ResponseEntity<Object> addBalance(@Valid @RequestBody BalanceRequest balanceRequest,
            HttpServletRequest httpServletRequest) {
        String login = authService.getLoginFromToken(httpServletRequest);
        userService.addBalance(login, balanceRequest.getBalance());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/balance")
    public ResponseEntity<Object> getUserBalance(HttpServletRequest httpServletRequest) {
        String login = authService.getLoginFromToken(httpServletRequest);
        return ResponseEntity.ok(new BalanceResponse(userService.getBalance(login), userService.getBonuses(login)));
    }

    @PatchMapping("/is-tutorial-completed")
    public ResponseEntity<Object> patchIsTutorialCompleted(@RequestBody Boolean isTutorialCompleted,
            HttpServletRequest httpServletRequest) {
        String login = authService.getLoginFromToken(httpServletRequest);
        userService.updateIsTutorialCompleted(login, isTutorialCompleted);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}