package com.hbsites.rpgtracker.infrastructure.repository;

import com.hbsites.rpgtracker.infrastructure.entity.CharacterSheetEntity;
import com.hbsites.rpgtracker.infrastructure.entity.CharacterSheetEntity_;
import com.hbsites.rpgtracker.infrastructure.entity.SessionEntity_;
import com.hbsites.rpgtracker.infrastructure.repository.interfaces.CharacterSheetRepository;
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
public class CharacterSheetRepositoryImpl implements CharacterSheetRepository {

    @Inject
    private NativeSql nativeSql;

    @Override
    public Uni<List<CharacterSheetEntity>> findAllByPlayerId(UUID playerId) {
        CharacterSheetEntity_ characterSheetEntity = new CharacterSheetEntity_();
        return Uni.createFrom().item(() -> nativeSql.from(characterSheetEntity).where(c -> c.eq(characterSheetEntity.playerId, playerId)).fetch());
    }

    @Override
    public Uni<CharacterSheetEntity> findOne(UUID sheetId) {
        CharacterSheetEntity_ characterSheetEntity = new CharacterSheetEntity_();
        return Uni.createFrom().item(() -> nativeSql.from(characterSheetEntity).where(c -> c.eq(characterSheetEntity.id, sheetId)).fetchOne());
    }

    @Override
    public Uni<List<UUID>> findAllCharacterSheetIdByPlayerId(UUID playerId) {
        CharacterSheetEntity_ characterSheetEntity = new CharacterSheetEntity_();
        return Uni.createFrom().item(() -> nativeSql.from(characterSheetEntity).where(c -> c.eq(characterSheetEntity.playerId, playerId))
            .selectAsRow(characterSheetEntity.id).stream().map(r -> r.get(characterSheetEntity.id))
            .toList()
        );
    }

    @Override
    public Uni<List<CharacterSheetEntity>> findAllBySessionId(UUID sessionId) {
        CharacterSheetEntity_ characterSheetEntity = new CharacterSheetEntity_();
        return Uni.createFrom().item(() -> nativeSql.from(characterSheetEntity).where(c -> c.eq(characterSheetEntity.sessionId, sessionId)).fetch());
    }
}
