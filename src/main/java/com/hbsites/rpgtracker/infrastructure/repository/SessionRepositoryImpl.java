package com.hbsites.rpgtracker.infrastructure.repository;

import com.hbsites.rpgtracker.infrastructure.entity.CharacterSheetEntity_;
import com.hbsites.rpgtracker.infrastructure.entity.SessionEntity;
import com.hbsites.rpgtracker.infrastructure.entity.SessionEntity_;
import com.hbsites.rpgtracker.infrastructure.repository.interfaces.SessionRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.seasar.doma.jdbc.criteria.NativeSql;

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
    public Uni<List<SessionEntity>> findAllByDmId(UUID dmId) {

        SessionEntity_ sessionEntity = new SessionEntity_();

        return Uni.createFrom().item(() -> nativeSql.from(sessionEntity).where(c -> c.eq(sessionEntity.dmId, dmId)).fetch());
    }

    @Override
    public Uni<List<SessionEntity>> findAllByPlayerId(UUID playerId) {
        CharacterSheetEntity_ characterSheetEntity = new CharacterSheetEntity_();
        SessionEntity_ sessionEntity = new SessionEntity_();
        return Uni.createFrom().item(() -> {
            List<UUID> entities = nativeSql.from(characterSheetEntity)
                .where(c -> c.eq(characterSheetEntity.playerId, playerId))
                .selectAsRow(characterSheetEntity.sessionId).stream().map(r -> r.get(characterSheetEntity.sessionId)).toList();
            return nativeSql.from(sessionEntity).where(c -> c.in(sessionEntity.sessionId, entities)).fetch();
        });
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
}
