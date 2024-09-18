package com.hbsites.rpgtracker.infrastructure.repository.interfaces;

import com.hbsites.rpgtracker.domain.dto.NextSessionsDTO;
import com.hbsites.rpgtracker.domain.enumeration.DmedFilter;
import com.hbsites.rpgtracker.infrastructure.entity.SessionEntity;
import io.smallrye.mutiny.Uni;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface SessionRepository {
    Uni<List<SessionEntity>> findAllByPlayerIdOrDmId(UUID userId, DmedFilter filter);

    Uni<Map<UUID, List<UUID>>>  findAllSessionIdsByDmId(UUID dmId);

    Uni<SessionEntity> findOneById(UUID sessionId);

    Uni<List<NextSessionsDTO>> findAllByDmIdOrPlayerIdWithDateRange(UUID userId, LocalDateTime startdate, LocalDateTime enddate, boolean limitinOne, DmedFilter filter);
}
