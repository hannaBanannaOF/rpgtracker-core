package com.hbsites.rpgtracker.core.dto;

import com.hbsites.hbsitescommons.dto.SessionListingDTO;
import com.hbsites.hbsitescommons.entity.BaseEntity;
import com.hbsites.hbsitescommons.enumeration.ETRPGSystem;
import com.hbsites.rpgtracker.core.entity.SessionEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class BasicSessionListingDTO extends SessionListingDTO<SessionEntity> {
    public BasicSessionListingDTO(UUID uuid, String sessionName, ETRPGSystem system, boolean inPlay, List<String> players) {
        this.setUuid(uuid);
        this.setSessionName(sessionName);
        this.setSystem(system);
        this.setInPlay(inPlay);
        this.setPlayers(players);
    }

    @Override
    public SessionEntity toEntity() {
        throw new NotImplementedException();
    }
}
