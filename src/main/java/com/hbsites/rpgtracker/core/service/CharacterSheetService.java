package com.hbsites.rpgtracker.core.service;

import com.hbsites.hbsitescommons.dto.CharacterSheetBasicInfoDTO;
import com.hbsites.hbsitescommons.dto.CharacterSheetListingDTO;
import com.hbsites.hbsitescommons.dto.UserDTO;
import com.hbsites.hbsitescommons.utils.UserUtils;
import com.hbsites.rpgtracker.core.entity.SessionEntity;
import com.hbsites.rpgtracker.core.producer.UserRequestProducer;
import com.hbsites.rpgtracker.core.repository.CharacterSheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class CharacterSheetService {

    @Autowired
    @Lazy
    private CharacterSheetRepository characterSheetRepository;

    @Autowired
    @Lazy
    private UserRequestProducer userRequestProducer;

    public List<CharacterSheetListingDTO<SessionEntity>> getAllCurrentUserSheets() {
        return characterSheetRepository.findAllByPlayerId(UserUtils.getUserUUID()).stream()
                .map(e -> {
                    CharacterSheetListingDTO<SessionEntity> dto = e.toListDTO();
                    UserDTO u = userRequestProducer.getUsersFromRabbit(List.of(e.getPlayerId())).stream().findFirst().orElse(new UserDTO());
                    dto.setUserName(u.getDisplayName());
                    return dto;
                }).collect(Collectors.toList());
    }

    public List<CharacterSheetBasicInfoDTO> getInfoById(List<UUID> ids) {
        return characterSheetRepository.findAllById(ids)
                .stream()
                .map(e -> {
                    CharacterSheetBasicInfoDTO dto = new CharacterSheetBasicInfoDTO();
                    dto.setCharacterName(e.getCharacterName());
                    UserDTO u = userRequestProducer.getUsersFromRabbit(List.of(e.getPlayerId())).stream().findFirst().orElse(new UserDTO());dto.setPlayer(u);
                    dto.setPlayer(u);
                    return dto;
                }).collect(Collectors.toList());
    }
}
