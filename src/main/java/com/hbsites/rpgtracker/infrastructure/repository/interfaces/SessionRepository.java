package com.hbsites.rpgtracker.infrastructure.repository.interfaces;

import com.hbsites.rpgtracker.domain.model.NextSessionItem;
import com.hbsites.rpgtracker.domain.enumeration.DmedFilter;
import com.hbsites.rpgtracker.infrastructure.database.entity.SessionEntity;
import io.smallrye.mutiny.Uni;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface SessionRepository {
    Uni<List<SessionEntity>> findAllByPlayerIdOrDmId(UUID userId, DmedFilter filter);

    Uni<SessionEntity> findOneBySlug(String slug);

    Uni<List<NextSessionItem>> findAllByDmIdOrPlayerIdWithDateRange(UUID userId, LocalDateTime startdate, LocalDateTime enddate, boolean limitinOne, DmedFilter filter);

    Boolean userCanSee(UUID userId, String slug);

    Uni<SessionEntity> updateInPlay(SessionEntity session);

    Uni<Void> scheduleSession(SessionEntity session, LocalDateTime dateTime);

    Uni<Void> deleteSchedule(Integer scheduleId);
}
