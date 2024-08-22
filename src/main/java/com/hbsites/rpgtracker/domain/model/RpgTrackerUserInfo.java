package com.hbsites.rpgtracker.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public class RpgTrackerUserInfo {
    private final List<UUID> userSheets;
    private final List<UUID> userDmedSessions;
    private final Map<UUID, List<UUID>> userDmedSessionsSheet;

    @Builder
    public RpgTrackerUserInfo(List<UUID> userSheets, Map<UUID, List<UUID>> userDmedSessionsSheet) {
        this.userSheets = userSheets;
        this.userDmedSessions = userDmedSessionsSheet.keySet().stream().toList();
        this.userDmedSessionsSheet = userDmedSessionsSheet;
    }

    public boolean sheetBelongsToUser(UUID sheetId) {
        boolean contains = false;
        for (List<UUID> sheets : userDmedSessionsSheet.values()) {
            contains = sheets.contains(sheetId);
            if (contains) break;
        }
        if (!contains) {
            return this.userSheets.contains(sheetId);
        }
        return contains;
    }

    public boolean sessionIsDmedByUser(UUID sessionId) {
        return this.userDmedSessions.contains(sessionId);
    }
}
