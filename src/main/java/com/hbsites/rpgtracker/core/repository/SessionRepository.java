package com.hbsites.rpgtracker.core.repository;

import com.hbsites.hbsitescommons.enumeration.ETRPGSystem;
import com.hbsites.rpgtracker.core.entity.SessionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, UUID> {
    Page<SessionEntity> findAllByDmId(UUID dmID, Pageable page);

    Boolean existsByDmIdAndTrpgSystem(UUID dmID, ETRPGSystem trpgSystem);
}
