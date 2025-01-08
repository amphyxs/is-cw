package com.par.parapp.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.par.parapp.dto.GuidesResponse;
import com.par.parapp.exception.ResourceNotFoundException;
import com.par.parapp.model.Game;
import com.par.parapp.model.Guide;
import com.par.parapp.model.User;
import com.par.parapp.repository.GuideRepository;

@Service
public class GuideService {
    private final ShopService shopService;

    private final GuideRepository guideRepository;

    public GuideService(GuideRepository guideRepository, ShopService shopService) {
        this.guideRepository = guideRepository;
        this.shopService = shopService;
    }

    public void saveGuide(User user, Game game, String guideText) {
        guideRepository.save(new Guide(user, game, guideText, new Timestamp(System.currentTimeMillis())));
    }

    public Page<Guide> getAllGuides(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return guideRepository.findAll(pageable);
    }

    public Page<Guide> getAllGuidesByGameName(String gameName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return guideRepository.findByGameName(gameName, pageable)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public Page<GuidesResponse> getGuidesByCondition(String selectedGame, int page, int size) {
        Page<Guide> guideList;
        if (selectedGame.isEmpty())
            guideList = getAllGuides(page, size);
        else
            guideList = getAllGuidesByGameName(selectedGame, page, size);

        List<GuidesResponse> guidesResponses = new ArrayList<>();
        guideList.forEach(guide -> guidesResponses.add(new GuidesResponse(guide.getUser().getLogin(),
                guide.getSendDate().toString().substring(0,
                        guide.getSendDate().toString().indexOf('.')),
                guide.getGuideText(),
                shopService.getGamePicture(guide.getGame().getName()), guide.getGame().getName())));
        guidesResponses.sort(Comparator.comparing(GuidesResponse::getSendDate));
        Collections.reverse(guidesResponses);

        return new PageImpl<>(guidesResponses, PageRequest.of(page, size), guideList.getTotalElements());
    }
}
