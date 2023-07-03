package com.hbsites.rpgtracker.core.service;

import com.hbsites.hbsitescommons.utils.UserUtils;
import com.hbsites.rpgtracker.core.dto.ConfigDTO;
import com.hbsites.rpgtracker.core.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {

    @Autowired
    @Lazy
    private SessionRepository sessionRepository;

    public ConfigDTO getUserConfiguration() {
        ConfigDTO config = new ConfigDTO();
        config.setDm(sessionRepository.existsByDmId(UserUtils.getUserUUID()));
        return config;
    }

}
