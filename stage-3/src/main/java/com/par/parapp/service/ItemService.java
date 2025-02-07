package com.par.parapp.service;

import com.par.parapp.exception.ResourceAlreadyExist;
import com.par.parapp.exception.ResourceNotFoundException;
import com.par.parapp.model.Game;
import com.par.parapp.model.Item;
import com.par.parapp.repository.ItemRepository;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void saveItem(Game game, String name, String rarity, String itemUrl) {
        if (itemRepository.existsItemByGameAndNameAndRarity(game.getId(), name, rarity).isPresent())
            throw new ResourceAlreadyExist(
                    "Вещь " + name + " с редкостью " + rarity + " уже существует у игры " + game.getName());

        itemRepository.save(new Item(game, name, rarity, itemUrl));
    }

    public Item getItemByGameNameItemNameAndRarity(Game game, String itemName, String rarity) {
        return itemRepository.existsItemByGameAndNameAndRarity(game.getId(), itemName, rarity)
                .orElseThrow(ResourceNotFoundException::new);

    }

}
