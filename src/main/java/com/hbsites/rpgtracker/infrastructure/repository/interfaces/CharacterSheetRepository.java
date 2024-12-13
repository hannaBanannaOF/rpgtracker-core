package com.hbsites.rpgtracker.infrastructure.repository.interfaces;

import com.hbsites.rpgtracker.infrastructure.database.entity.CharacterSheetEntity;
import io.smallrye.mutiny.Uni;

import java.util.List;
import java.util.UUID;

public interface CharacterSheetRepository {

    Uni<List<CharacterSheetEntity>> findAllByPlayerId(UUID playerId);

    Uni<CharacterSheetEntity> findOneBySlug(String slug);

    Uni<List<CharacterSheetEntity>> findAllBySessionSlug(String slug);

    Uni<Boolean> userCanSee(UUID userId, String slug);

}
