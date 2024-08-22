package com.hbsites.rpgtracker.infrastructure.repository.interfaces;

import com.hbsites.rpgtracker.infrastructure.entity.SessionEntity;
import io.smallrye.mutiny.Uni;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface SessionRepository {
    Uni<List<SessionEntity>> findAllByDmId(UUID dmId);
    Uni<List<SessionEntity>> findAllByPlayerId(UUID playerId);

    Uni<Map<UUID, List<UUID>>>  findAllSessionIdsByDmId(UUID dmId);

     Uni<SessionEntity> findOneById(UUID sessionId);
}
