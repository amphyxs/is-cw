package com.par.parapp.controller;

import com.par.parapp.service.AuthService;
import com.par.parapp.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/inventory")
@CrossOrigin(origins = "*", maxAge = 3600)
public class InventoryController {

    private final InventoryService inventoryService;

    private final AuthService authService;

    public InventoryController(InventoryService inventoryService, AuthService authService) {
        this.inventoryService = inventoryService;
        this.authService = authService;
    }

    @GetMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> getAllItems(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest httpServletRequest) {
        String login = authService.getLoginFromToken(httpServletRequest);

        return ResponseEntity.ok(inventoryService.getAllItems(login, page, size));
    }

}
