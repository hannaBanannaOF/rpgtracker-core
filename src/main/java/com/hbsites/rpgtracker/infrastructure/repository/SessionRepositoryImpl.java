package com.hbsites.rpgtracker.infrastructure.repository;

import com.hbsites.rpgtracker.domain.dto.NextSessionsDTO;
import com.hbsites.rpgtracker.domain.enumeration.DmedFilter;
import com.hbsites.rpgtracker.infrastructure.entity.CharacterSheetEntity_;
import com.hbsites.rpgtracker.infrastructure.entity.SessionCalendarEntity_;
import com.hbsites.rpgtracker.infrastructure.entity.SessionEntity;
import com.hbsites.rpgtracker.infrastructure.entity.SessionEntity_;
import com.hbsites.rpgtracker.infrastructure.repository.interfaces.SessionRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.seasar.doma.jdbc.criteria.NativeSql;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;
import org.seasar.doma.jdbc.criteria.statement.NativeSqlSelectStarting;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@ApplicationScoped
public class SessionRepositoryImpl implements SessionRepository {

    @Inject
    private NativeSql nativeSql;

    @Override
    public Uni<List<SessionEntity>> findAllByPlayerIdOrDmId(UUID userId, DmedFilter filter) {
        CharacterSheetEntity_ characterSheetEntity = new CharacterSheetEntity_();
        SessionEntity_ sessionEntity = new SessionEntity_();
        return Uni.createFrom().item(() -> nativeSql.from(sessionEntity).distinct()
            .leftJoin(characterSheetEntity, on -> on.eq(characterSheetEntity.sessionId, sessionEntity.sessionId))
            .where(c -> whereByDmedFilter(filter, c, sessionEntity, characterSheetEntity, userId))
            .fetch()
        );
    }

    @Override
    public Uni<Map<UUID, List<UUID>>> findAllSessionIdsByDmId(UUID dmId) {
        CharacterSheetEntity_ characterSheetEntity = new CharacterSheetEntity_();
        SessionEntity_ sessionEntity = new SessionEntity_();
        return Uni.createFrom().item(() -> {
            Map<UUID, List<UUID>> combine = new HashMap<>();
            var result = nativeSql.from(sessionEntity)
                    .leftJoin(characterSheetEntity, on -> on.eq(sessionEntity.sessionId, characterSheetEntity.sessionId))
                    .where(c -> {
                        c.eq(sessionEntity.dmId, dmId);
                    })
                    .selectAsRow(characterSheetEntity.id, sessionEntity.sessionId)
                    .fetch();
            result.forEach(r -> {
                UUID sessionId = r.get(sessionEntity.sessionId);
                if (!combine.containsKey(sessionId)) {
                    combine.put(sessionId, new ArrayList<>());
                }
                combine.get(sessionId).add(r.get(characterSheetEntity.id));
            });
            return combine;
        });
    }

    @Override
    public Uni<SessionEntity> findOneById(UUID sessionId) {
        SessionEntity_ sessionEntity = new SessionEntity_();
        return Uni.createFrom().item(() -> nativeSql.from(sessionEntity).where(c -> c.eq(sessionEntity.sessionId, sessionId)).fetchOne());
    }

    @Override
    public Uni<List<NextSessionsDTO>> findAllByDmIdOrPlayerIdWithDateRange(UUID userId, LocalDateTime startdate, LocalDateTime enddate, boolean limitinOne, DmedFilter filter) {
        SessionEntity_ sessionEntity = new SessionEntity_();
        CharacterSheetEntity_ characterSheetEntity = new CharacterSheetEntity_();
        SessionCalendarEntity_ sessionCalendarEntity = new SessionCalendarEntity_();
        var sql = nativeSql.from(sessionEntity).distinct()
            .leftJoin(characterSheetEntity, on -> on.eq(characterSheetEntity.sessionId, sessionEntity.sessionId))
            .leftJoin(sessionCalendarEntity, on -> on.eq(sessionCalendarEntity.sessionId, sessionEntity.sessionId))
            .where(c -> {
                c.isNotNull(sessionCalendarEntity.sessionDate);
                if (enddate != null) {
                    c.le(sessionCalendarEntity.sessionDate, enddate);
                }
                c.ge(sessionCalendarEntity.sessionDate, startdate);
                c.ge(sessionCalendarEntity.sessionDate, LocalDateTime.now());
                c.and(() -> whereByDmedFilter(filter, c, sessionEntity, characterSheetEntity, userId));
            })
            .orderBy(o -> o.asc(sessionCalendarEntity.sessionDate));
        if (limitinOne) {
            sql = sql.limit(1);
        }
        NativeSqlSelectStarting<SessionEntity> finalSql = sql;
        return Uni.createFrom().item(() -> finalSql.selectAsRow(sessionEntity.sessionId, sessionEntity.sessionName, sessionEntity.dmId, sessionEntity.trpgSystem, sessionCalendarEntity.sessionDate).fetch()
            .stream()
            .map(r -> NextSessionsDTO.builder()
                .nextSessionDate(r.get(sessionCalendarEntity.sessionDate))
                .nextSessionId(r.get(sessionEntity.sessionId))
                .nextSessionName(r.get(sessionEntity.sessionName))
                .nextSessionSystem(r.get(sessionEntity.trpgSystem))
                .nextSessionDmed(r.get(sessionEntity.dmId).equals(userId))
                .build()
            ).toList()
        );
    }

    private void whereByDmedFilter(DmedFilter filter, WhereDeclaration c, SessionEntity_ sessionEntity, CharacterSheetEntity_ characterSheetEntity, UUID userId) {
        switch (filter) {
            case ONLY_DM: c.eq(sessionEntity.dmId, userId);break;
            case ONLY_PLAYER: c.eq(characterSheetEntity.playerId, userId);break;
            default:
                c.eq(sessionEntity.dmId, userId);
                c.or(() -> c.eq(characterSheetEntity.playerId, userId));
                break;
        }
    }
}
