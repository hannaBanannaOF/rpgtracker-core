package com.hbsites.rpgtracker.infrastructure.repository;

import com.hbsites.rpgtracker.infrastructure.database.entity.CharacterSheetEntity;
import com.hbsites.rpgtracker.infrastructure.database.entity.CharacterSheetEntity_;
import com.hbsites.rpgtracker.infrastructure.database.entity.SessionEntity_;
import com.hbsites.rpgtracker.infrastructure.repository.interfaces.CharacterSheetRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.seasar.doma.jdbc.criteria.NativeSql;

import java.util.List;
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
    public Uni<CharacterSheetEntity> findOneBySlug(String slug) {
        CharacterSheetEntity_ characterSheetEntity = new CharacterSheetEntity_();
        return Uni.createFrom().item(() -> nativeSql.from(characterSheetEntity).where(c -> c.eq(characterSheetEntity.slug, slug)).fetchOne());
    }

    @Override
    public Uni<List<CharacterSheetEntity>> findAllBySessionSlug(String slug) {
        CharacterSheetEntity_ characterSheetEntity = new CharacterSheetEntity_();
        SessionEntity_ sessionEntity = new SessionEntity_();
        return Uni.createFrom().item(() -> nativeSql.from(characterSheetEntity)
                .leftJoin(sessionEntity, on -> on.eq(sessionEntity.id, characterSheetEntity.sessionId))
                .where(c -> c.eq(sessionEntity.slug, slug)).fetch());
    }

    @Override
    public Uni<Boolean> userCanSee(UUID userId, String slug) {
        CharacterSheetEntity_ characterSheetEntity = new CharacterSheetEntity_();
        SessionEntity_ sessionEntity = new SessionEntity_();
        return Uni.createFrom().item(() -> nativeSql.from(characterSheetEntity)
                .leftJoin(sessionEntity, on -> on.eq(sessionEntity.id, characterSheetEntity.sessionId))
                .where(c -> {
                    c.eq(characterSheetEntity.slug, slug);
                    c.eq(characterSheetEntity.playerId, userId);
                    c.or(() -> c.eq(sessionEntity.dmId, userId));
                }).stream().findAny().isPresent());
    }
}
