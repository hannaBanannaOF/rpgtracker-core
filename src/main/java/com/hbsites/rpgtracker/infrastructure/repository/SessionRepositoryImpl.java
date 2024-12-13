package com.hbsites.rpgtracker.infrastructure.repository;

import com.hbsites.rpgtracker.domain.model.NextSessionItem;
import com.hbsites.rpgtracker.domain.enumeration.DmedFilter;
import com.hbsites.rpgtracker.infrastructure.database.entity.CharacterSheetEntity_;
import com.hbsites.rpgtracker.infrastructure.database.entity.SessionCalendarEntity_;
import com.hbsites.rpgtracker.infrastructure.database.entity.SessionEntity;
import com.hbsites.rpgtracker.infrastructure.database.entity.SessionEntity_;
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

@ApplicationScoped
public class SessionRepositoryImpl implements SessionRepository {

    @Inject
    private NativeSql nativeSql;

    @Override
    public Uni<List<SessionEntity>> findAllByPlayerIdOrDmId(UUID userId, DmedFilter filter) {
        CharacterSheetEntity_ characterSheetEntity = new CharacterSheetEntity_();
        SessionEntity_ sessionEntity = new SessionEntity_();
        return Uni.createFrom().item(() -> nativeSql.from(sessionEntity).distinct()
            .leftJoin(characterSheetEntity, on -> on.eq(characterSheetEntity.sessionId, sessionEntity.id))
            .where(c -> whereByDmedFilter(filter, c, sessionEntity, characterSheetEntity, userId))
            .fetch()
        );
    }

    @Override
    public Uni<SessionEntity> findOneBySlug(String slug) {
        SessionEntity_ sessionEntity = new SessionEntity_();
        return Uni.createFrom().item(() -> nativeSql.from(sessionEntity).where(c -> c.eq(sessionEntity.slug, slug)).fetchOne());
    }

    @Override
    public Uni<List<NextSessionItem>> findAllByDmIdOrPlayerIdWithDateRange(UUID userId, LocalDateTime startdate, LocalDateTime enddate, boolean limitinOne, DmedFilter filter) {
        SessionEntity_ sessionEntity = new SessionEntity_();
        CharacterSheetEntity_ characterSheetEntity = new CharacterSheetEntity_();
        SessionCalendarEntity_ sessionCalendarEntity = new SessionCalendarEntity_();
        var sql = nativeSql.from(sessionEntity).distinct()
            .leftJoin(characterSheetEntity, on -> on.eq(characterSheetEntity.sessionId, sessionEntity.id))
            .leftJoin(sessionCalendarEntity, on -> on.eq(sessionCalendarEntity.sessionId, sessionEntity.id))
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
        return Uni.createFrom().item(() -> finalSql.selectAsRow(sessionEntity.slug, sessionEntity.sessionName, sessionEntity.dmId, sessionEntity.trpgSystem, sessionCalendarEntity.sessionDate).fetch()
            .stream()
            .map(r ->
                    new NextSessionItem(r.get(sessionEntity.slug), r.get(sessionEntity.sessionName), r.get(sessionEntity.trpgSystem), r.get(sessionCalendarEntity.sessionDate), r.get(sessionEntity.dmId).equals(userId))
            ).toList()
        );
    }

    @Override
    public Uni<Boolean> userCanSee(UUID userId, String slug) {
        SessionEntity_ sessionEntity = new SessionEntity_();
        CharacterSheetEntity_ characterSheetEntity = new CharacterSheetEntity_();
        return Uni.createFrom().item(() -> nativeSql.from(sessionEntity)
                .leftJoin(characterSheetEntity, on -> on.eq(characterSheetEntity.sessionId, sessionEntity.id))
                .where(c -> {
                    c.eq(sessionEntity.slug, slug);
                    c.eq(sessionEntity.dmId, userId);
                    c.or(() -> c.eq(characterSheetEntity.playerId, userId));
                }).stream().findAny().isPresent());
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
