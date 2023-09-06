package com.hbsites.rpgtracker.core.service;

import com.hbsites.hbsitescommons.dto.SessionBasicInfoDTO;
import com.hbsites.hbsitescommons.dto.SessionSheetDTO;
import com.hbsites.hbsitescommons.utils.UserUtils;
import com.hbsites.rpgtracker.core.dto.BasicSessionListingDTO;
import com.hbsites.rpgtracker.core.entity.SessionEntity;
import com.hbsites.rpgtracker.core.producer.UserRequestProducer;
import com.hbsites.rpgtracker.core.repository.SessionRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
                .map(e -> e.toListDTO(userRequestProducer))
                .collect(Collectors.toList());
    }

    public List<SessionBasicInfoDTO> getInfoById(List<UUID> uuids) {
        return sessionRepository.findAllById(uuids)
                .stream()
                .map(e -> {
                    Hibernate.initialize(e.getSheets());
                    SessionBasicInfoDTO dto = new SessionBasicInfoDTO();
                    dto.setSessionName(e.getSessionName());
                    dto.setCoreId(e.getSessionId());
                    dto.setSessionSheets(e.getSheets().stream().map(e2 -> new SessionSheetDTO(e2.getId(), e2.getCharacterName())).collect(Collectors.toList()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

}
