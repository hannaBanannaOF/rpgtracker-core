package com.hbsites.rpgtracker.core.service;

import com.hbsites.hbsitescommons.dto.UserDTO;
import com.hbsites.hbsitescommons.utils.UserUtils;
import com.hbsites.rpgtracker.core.dto.BasicSessionListingDTO;
import com.hbsites.rpgtracker.core.entity.CharacterSheetEntity;
import com.hbsites.rpgtracker.core.producer.UserRequestProducer;
import com.hbsites.rpgtracker.core.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SessionService {

    @Lazy
    @Autowired
    private SessionRepository sessionRepository;

    @Lazy
    @Autowired
    private UserRequestProducer userRequestProducer;

    public List<BasicSessionListingDTO> getDMedSessions() {
        return sessionRepository.findAllByDmId(UserUtils.getUserUUID())
                .stream()
                .map(e -> {
                    BasicSessionListingDTO dto = e.toListDTO();

                    // Populate player list
                    List<UUID> playerIds = e.getSheets().stream().map(CharacterSheetEntity::getPlayerId).toList();
                    HashSet<UUID> playerIdsUnique = new HashSet<UUID>(playerIds);

                    List<UserDTO> users = userRequestProducer.getUsersFromRabbit(playerIdsUnique.stream().toList());

                    dto.setPlayers(playerIdsUnique.stream().map(uuid ->
                            users.stream().filter(us -> us.getUuid().equals(uuid)).findFirst().orElse(new UserDTO()).getDisplayName()
                    ).collect(Collectors.toList()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

}
