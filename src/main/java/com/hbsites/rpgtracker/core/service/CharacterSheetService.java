package com.hbsites.rpgtracker.core.service;

import com.hbsites.hbsitescommons.dto.CharacterSheetBasicInfoDTO;
import com.hbsites.hbsitescommons.dto.CharacterSheetListingDTO;
import com.hbsites.hbsitescommons.utils.UserUtils;
import com.hbsites.rpgtracker.core.entity.SessionEntity;
import com.hbsites.rpgtracker.core.repository.CharacterSheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class CharacterSheetService {

    @Autowired
    @Lazy
    private CharacterSheetRepository characterSheetRepository;

    public Page<CharacterSheetListingDTO<SessionEntity>> getAllCurrentUserSheets(int page) {
        Pageable pageRequest = PageRequest.of(page, 20);
        return characterSheetRepository.findAllByPlayerId(UserUtils.getUserUUID(), pageRequest)
                .map(e -> e.toListDTO(null));
    }

    public List<CharacterSheetBasicInfoDTO> getInfoById(List<UUID> ids) {
        return characterSheetRepository.findAllById(ids)
                .stream()
                .map(e -> {
                    CharacterSheetBasicInfoDTO dto = new CharacterSheetBasicInfoDTO();
                    dto.setCharacterName(e.getCharacterName());
                    dto.setSheetId(e.getId());
                    dto.setPlayerId(e.getPlayerId());
                    return dto;
                }).collect(Collectors.toList());
    }
}
