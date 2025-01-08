package com.par.parapp.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.par.parapp.dto.SlotsResponse;
import com.par.parapp.exception.ResourceNotFoundException;
import com.par.parapp.model.Item;
import com.par.parapp.model.Market;
import com.par.parapp.model.User;
import com.par.parapp.repository.MarketRepository;

@Service
public class MarketService {

    private final MarketRepository marketRepository;

    public MarketService(MarketRepository marketRepository) {
        this.marketRepository = marketRepository;
    }

    public void save(User user, Item item, Double price) {
        marketRepository.save(new Market(user, item, price));
    }

    public Market getMarketById(Long id) {
        return marketRepository.getMarketById(id).orElseThrow(ResourceNotFoundException::new);
    }

    public Page<SlotsResponse> getEntriesByGameNameAndItemName(String itemName, String gameName, int page, int size) {
        List<Market> marketList;
        if (itemName.isEmpty())
            marketList = marketRepository.getAllSlots().orElseThrow(ResourceNotFoundException::new);
        else
            marketList = marketRepository.getAllFromMarketByItemNameFilter(itemName)
                    .orElseThrow(ResourceNotFoundException::new);

        List<SlotsResponse> slotsResponses = new ArrayList<>();

        if (!gameName.isEmpty()) {
            marketList.forEach(market -> {
                if (market.getItem().getGame().getName().equals(gameName))
                    slotsResponses.add(new SlotsResponse(market.getItem().getName(),
                            market.getItem().getItemUrl(),
                            market.getItem().getGame().getName(),
                            market.getItem().getRarity(),
                            market.getPrice(), market.getId()));
            });
        } else
            marketList.forEach(market -> slotsResponses.add(new SlotsResponse(market.getItem().getName(),
                    market.getItem().getItemUrl(),
                    market.getItem().getGame().getName(),
                    market.getItem().getRarity(),
                    market.getPrice(), market.getId())));

        slotsResponses.sort(Comparator.comparing(SlotsResponse::getPrice));

        return new PageImpl<>(slotsResponses, PageRequest.of(page, size), slotsResponses.size());

    }

    public void deleteSlot(Long marketId) {
        marketRepository.deleteMarketById(marketId);
    }

}
