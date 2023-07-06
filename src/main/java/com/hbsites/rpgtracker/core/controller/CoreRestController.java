package com.hbsites.rpgtracker.core.controller;

import com.hbsites.hbsitescommons.config.ApiVersion;
import com.hbsites.hbsitescommons.dto.CharacterSheetListingDTO;
import com.hbsites.rpgtracker.core.dto.BasicSessionListingDTO;
import com.hbsites.rpgtracker.core.dto.ConfigDTO;
import com.hbsites.rpgtracker.core.entity.SessionEntity;
import com.hbsites.rpgtracker.core.service.CharacterSheetService;
import com.hbsites.rpgtracker.core.service.ConfigurationService;
import com.hbsites.rpgtracker.core.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@ApiVersion(1)
public class CoreRestController {

    @Autowired
    @Lazy
    private CharacterSheetService characterSheetService;

    @Autowired
    @Lazy
    private ConfigurationService configurationService;

    @Autowired
    @Lazy
    private SessionService sessionService;

    @GetMapping("/user-config")
    public ConfigDTO getUserConfig() {
        return configurationService.getUserConfiguration();
    }

    @GetMapping("/my-dm-sessions")
    public List<BasicSessionListingDTO> getMySessions() {
        return sessionService.getDMedSessions();
    }

    @GetMapping("/my-character-sheets")
    public List<CharacterSheetListingDTO<SessionEntity>> getMyCharacterSheets() {
        return characterSheetService.getAllCurrentUserSheets();
    }
}
