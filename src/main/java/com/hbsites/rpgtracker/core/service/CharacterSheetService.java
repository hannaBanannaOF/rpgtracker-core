package com.hbsites.rpgtracker.core.service;

import com.hbsites.hbsitescommons.dto.CharacterSheetListingDTO;
import com.hbsites.hbsitescommons.utils.UserUtils;
import com.hbsites.rpgtracker.core.entity.CharacterSheetEntity;
import com.hbsites.rpgtracker.core.repository.CharacterSheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class CharacterSheetService {

    @Autowired
    @Lazy
    private CharacterSheetRepository characterSheetRepository;

    public List<CharacterSheetListingDTO> getAllCurrentUserSheets() {
        return characterSheetRepository.findAllByPlayerId(UserUtils.getUserUUID()).stream()
                .map(CharacterSheetEntity::toListDTO).collect(Collectors.toList());
    }
}
