package com.par.parapp.controller;

import com.par.parapp.service.ShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ShopController {
    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping()
    public ResponseEntity<Object> getGamesByNameAndGenres(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, @RequestParam String gameName,
            @RequestParam List<String> genres) {
        return ResponseEntity.ok(shopService.getEntriesByGameNameAndGenres(gameName,
                genres, page, size));

    }

}
