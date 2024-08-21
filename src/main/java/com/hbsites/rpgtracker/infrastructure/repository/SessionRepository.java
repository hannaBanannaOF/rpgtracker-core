package com.hbsites.rpgtracker.infrastructure.repository;

import com.hbsites.commons.rpgtracker.infrastructure.entity.SessionEntity;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class SessionRepository implements PanacheRepository<SessionEntity> {

    public Uni<List<SessionEntity>> findAllByDmId(UUID dmId) {
        return find("from SessionEntity as session where session.dmId = ?1", dmId).withLock(LockModeType.NONE).list();
    }
    public Uni<List<SessionEntity>> findAllByPlayerId(UUID playerId) {
        return find("from SessionEntity as session left join session.sheets as sheet where sheet.playerId = ?1", playerId).withLock(LockModeType.NONE).list();
    }

    public Uni<List<SessionEntity>> findAllByPlayerIdOrPlayerId(UUID playerId) {
        return find("from SessionEntity as session left join session.sheets as sheet where sheet.playerId = ?1 or session.dmId = ?1", playerId).withLock(LockModeType.NONE).list();
    }
}
