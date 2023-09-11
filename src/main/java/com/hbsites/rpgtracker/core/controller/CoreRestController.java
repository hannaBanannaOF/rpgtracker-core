package com.hbsites.rpgtracker.core.controller;

import com.hbsites.hbsitescommons.config.ApiVersion;
import com.hbsites.hbsitescommons.dto.CharacterSheetBasicInfoDTO;
import com.hbsites.hbsitescommons.dto.CharacterSheetListingDTO;
import com.hbsites.hbsitescommons.dto.SessionBasicInfoDTO;
import com.hbsites.hbsitescommons.utils.UserUtils;
import com.hbsites.rpgtracker.core.dto.BasicSessionListingDTO;
import com.hbsites.rpgtracker.core.dto.ConfigDTO;
import com.hbsites.rpgtracker.core.entity.SessionEntity;
import com.hbsites.rpgtracker.core.service.CharacterSheetService;
import com.hbsites.rpgtracker.core.service.ConfigurationService;
import com.hbsites.rpgtracker.core.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

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
    public Page<BasicSessionListingDTO> getMySessions(@RequestParam(
            value = "page",
            required = false,
            defaultValue = "0") int page) {
        return sessionService.getDMedSessions(page);
    }

    @GetMapping("/my-character-sheets")
    public Page<CharacterSheetListingDTO<SessionEntity>> getMyCharacterSheets(@RequestParam(
            value = "page",
            required = false,
            defaultValue = "0") int page) {
        return characterSheetService.getAllCurrentUserSheets(page);
    }

    @GetMapping("/character-sheet/{uuid}")
    public void getSheetBasicInfo(@PathVariable UUID uuid) {
        List<CharacterSheetBasicInfoDTO> infos = characterSheetService.getInfoById(List.of(uuid));
        if (infos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!infos.get(0).getPlayerId().equals(UserUtils.getUserUUID())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/session/{uuid}")
    public void getSessionBasicInfo(@PathVariable UUID uuid) {
        List<SessionBasicInfoDTO> infos = sessionService.getInfoById(List.of(uuid));
        if (infos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!infos.get(0).getDmId().equals(UserUtils.getUserUUID())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
