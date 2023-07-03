package com.hbsites.rpgtracker.core.dto;

import com.hbsites.rpgtracker.core.entity.SessionEntity;
import com.hbsites.rpgtracker.core.enumeration.ETRPGSystem;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public abstract class SessionListingDTO {
    private UUID uuid;
    private String sessionName;
    private ETRPGSystem system;
    private boolean inPlay;
    private List<String> players;

    public void populate(UUID uuid, String sessionName, ETRPGSystem system, boolean inPlay, List<String> players) {
        this.setUuid(uuid);
        this.setSessionName(sessionName);
        this.setSystem(system);
        this.setInPlay(inPlay);
        this.setPlayers(players);
    }

    public abstract SessionEntity toEntity();

    public BasicSessionListingDTO asBasic() {
        return new BasicSessionListingDTO(this.getUuid(), this.getSessionName(), this.getSystem(), this.isInPlay(), this.getPlayers());
    }
}
