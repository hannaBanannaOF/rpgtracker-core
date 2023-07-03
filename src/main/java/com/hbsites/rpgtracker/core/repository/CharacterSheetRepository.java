package com.hbsites.rpgtracker.core.repository;

import com.hbsites.rpgtracker.core.entity.CharacterSheetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CharacterSheetRepository extends JpaRepository<CharacterSheetEntity, UUID> {
    List<CharacterSheetEntity> findAllByPlayerId(UUID playerId);
}
