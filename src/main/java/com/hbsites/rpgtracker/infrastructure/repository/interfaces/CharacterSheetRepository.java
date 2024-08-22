package com.hbsites.rpgtracker.infrastructure.repository.interfaces;

import com.hbsites.rpgtracker.infrastructure.entity.CharacterSheetEntity;
import io.smallrye.mutiny.Uni;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CharacterSheetRepository {

    Uni<List<CharacterSheetEntity>> findAllByPlayerId(UUID playerId);

    Uni<CharacterSheetEntity> findOne(UUID sheetId);

    Uni<List<UUID>> findAllCharacterSheetIdByPlayerId(UUID playerId);

    Uni<List<CharacterSheetEntity>> findAllBySessionId(UUID sessionId);

}
