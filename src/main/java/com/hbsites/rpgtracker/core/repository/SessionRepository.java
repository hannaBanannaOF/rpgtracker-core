package com.hbsites.rpgtracker.core.repository;

import com.hbsites.rpgtracker.core.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, UUID> {
    List<SessionEntity> findAllByDmId(UUID dmID);

    Boolean existsByDmId(UUID dmID);
}
