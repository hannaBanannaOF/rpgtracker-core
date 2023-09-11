package com.hbsites.rpgtracker.core.repository;

import com.hbsites.hbsitescommons.enumeration.ETRPGSystem;
import com.hbsites.rpgtracker.core.entity.CharacterSheetEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CharacterSheetRepository extends JpaRepository<CharacterSheetEntity, UUID> {
    Page<CharacterSheetEntity> findAllByPlayerId(UUID playerId, Pageable pageable);

    Boolean existsByPlayerIdAndTrpgSystem(UUID playerId, ETRPGSystem trpgSystem);
}
