package com.hbsites.rpgtracker.core.service;

import com.hbsites.hbsitescommons.commons.utils.UserUtils;
import com.hbsites.hbsitescommons.rpgtracker.dto.SessionBasicInfoDTO;
import com.hbsites.hbsitescommons.rpgtracker.dto.SessionSheetDTO;
import com.hbsites.rpgtracker.core.dto.BasicSessionListingDTO;
import com.hbsites.rpgtracker.core.producer.UserRequestProducer;
import com.hbsites.rpgtracker.core.repository.SessionRepository;
import org.hibernate.Hibernate;
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
public class SessionService {

    @Lazy
    @Autowired
    private SessionRepository sessionRepository;

    @Lazy
    @Autowired
    private UserRequestProducer userRequestProducer;

    public Page<BasicSessionListingDTO> getDMedSessions(int page) {
        Pageable pageRequest = PageRequest.of(page, 20);
        return sessionRepository.findAllByDmId(UserUtils.getUserUUID(), pageRequest)
                .map(e -> e.toListDTO(userRequestProducer));
    }

    public List<SessionBasicInfoDTO> getInfoById(List<UUID> uuids) {
        return sessionRepository.findAllById(uuids)
                .stream()
                .map(e -> {
                    Hibernate.initialize(e.getSheets());
                    SessionBasicInfoDTO dto = new SessionBasicInfoDTO();
                    dto.setDmId(e.getDmId());
                    dto.setSessionName(e.getSessionName());
                    dto.setCoreId(e.getSessionId());
                    dto.setSessionSheets(e.getSheets().stream().map(e2 -> new SessionSheetDTO(e2.getId(), e2.getCharacterName())).collect(Collectors.toList()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

}
