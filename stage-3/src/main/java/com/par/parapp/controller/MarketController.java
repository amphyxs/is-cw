package com.par.parapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.par.parapp.dto.ItemBuyRequest;
import com.par.parapp.dto.ItemSellRequest;
import com.par.parapp.model.Game;
import com.par.parapp.model.Item;
import com.par.parapp.model.Market;
import com.par.parapp.model.User;
import com.par.parapp.service.AuthService;
import com.par.parapp.service.GameService;
import com.par.parapp.service.InventoryService;
import com.par.parapp.service.ItemService;
import com.par.parapp.service.MarketService;
import com.par.parapp.service.UserService;

@Validated
@RestController
@RequestMapping("/market")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MarketController {

    private final UserService userService;
    private final MarketService marketService;

    private final ItemService itemService;

    private final GameService gameService;

    private final InventoryService inventoryService;

    private final AuthService authService;

    public MarketController(UserService userService, MarketService marketService, ItemService itemService,
            GameService gameService, InventoryService inventoryService, AuthService authService) {
        this.userService = userService;
        this.marketService = marketService;
        this.itemService = itemService;
        this.gameService = gameService;
        this.inventoryService = inventoryService;
        this.authService = authService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/sell")
    public ResponseEntity<Object> sellItem(@Valid @RequestBody ItemSellRequest itemSellRequest,
            HttpServletRequest httpServletRequest) {
        String login = authService.getLoginFromToken(httpServletRequest);
        User user = userService.getUserByLogin(login);
        Game game = gameService.getGameByName(itemSellRequest.getGameName());
        Item item = itemService.getItemByGameNameItemNameAndRarity(game, itemSellRequest.getItemName(),
                itemSellRequest.getRarity());
        marketService.save(user, item, itemSellRequest.getPrice());
        inventoryService.removeItemFromSeller(user, item);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<Object> getAllSlots(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String gameName,
            @RequestParam String itemName) {
        return ResponseEntity.ok(marketService.getEntriesByGameNameAndItemName(itemName, gameName, page, size));

    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/buy")
    public ResponseEntity<Object> buyItem(@RequestBody ItemBuyRequest itemBuyRequest,
            HttpServletRequest httpServletRequest) {
        String login = authService.getLoginFromToken(httpServletRequest);
        User user = userService.getUserByLogin(login);
        Market market = marketService.getMarketById(itemBuyRequest.getMarketId());

        userService.checkBalanceToBuyGame(user, market.getPrice());

        Game game = gameService.getGameByName(itemBuyRequest.getGameName());
        Item item = itemService.getItemByGameNameItemNameAndRarity(game,
                itemBuyRequest.getItemName(), itemBuyRequest.getRarity());
        inventoryService.saveItemToCustomer(user, item);

        userService.chargeBalanceCustomer(login, market.getPrice(), item.getId());

        marketService.deleteSlot(market.getId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
