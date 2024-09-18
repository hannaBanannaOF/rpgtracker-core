package com.hbsites.rpgtracker.domain.dto;

import com.hbsites.commons.rpgtracker.domain.enumeration.ETRPGSystem;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class NextSessionsDTO {
    private UUID nextSessionId;
    private String nextSessionName;
    private ETRPGSystem nextSessionSystem;
    private LocalDateTime nextSessionDate;
    private boolean nextSessionDmed;
}
