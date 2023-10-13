package com.hbsites.rpgtracker.core.service;

import com.hbsites.hbsitescommons.rpgtracker.enumeration.ETRPGSystem;
import com.hbsites.hbsitescommons.commons.utils.UserUtils;
import com.hbsites.rpgtracker.core.dto.ConfigDTO;
import com.hbsites.rpgtracker.core.repository.CharacterSheetRepository;
import com.hbsites.rpgtracker.core.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ConfigurationService {

    @Autowired
    @Lazy
    private SessionRepository sessionRepository;

    @Autowired
    @Lazy
    private CharacterSheetRepository characterSheetRepository;

    public ConfigDTO getUserConfiguration() {
        ConfigDTO config = new ConfigDTO();
        UUID userUuid = UserUtils.getUserUUID();
        config.setCocDm(sessionRepository.existsByDmIdAndTrpgSystem(userUuid, ETRPGSystem.CALL_OF_CTHULHU));
        config.setHasCocSheet(characterSheetRepository.existsByPlayerIdAndTrpgSystem(userUuid, ETRPGSystem.CALL_OF_CTHULHU));
        return config;
    }

}
