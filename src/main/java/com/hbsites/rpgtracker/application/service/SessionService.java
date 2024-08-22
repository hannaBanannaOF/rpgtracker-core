package com.hbsites.rpgtracker.application.service;

import com.hbsites.commons.domain.dto.BasicListDTO;
import com.hbsites.rpgtracker.domain.dto.BasicSessionListDTO;
import com.hbsites.rpgtracker.domain.dto.SessionDetailsDTO;
import com.hbsites.rpgtracker.domain.params.SessionListParams;
import com.hbsites.rpgtracker.domain.params.SessionParams;
import com.hbsites.rpgtracker.infrastructure.entity.SessionEntity;
import com.hbsites.rpgtracker.infrastructure.repository.interfaces.CharacterSheetRepository;
import com.hbsites.rpgtracker.infrastructure.repository.interfaces.SessionRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class SessionService {

    @Inject
    SessionRepository sessionRepository;

    @Inject
    CharacterSheetRepository characterSheetRepository;

    @Inject
    JsonWebToken token;

    public Uni<List<BasicSessionListDTO>> getMySessions(SessionListParams params) {
        return getMySessionsV1(params);
    }

    public Uni<SessionDetailsDTO> findSessionById(SessionParams params) {
        return findSessionByIdV1(params.getSessionId());
    }

    private Uni<List<BasicSessionListDTO>> getMySessionsV1(SessionListParams params) {
        UUID userId = UUID.fromString(token.getSubject());
        Uni<List<SessionEntity>> sessions = switch (params.getDmedFilter()) {
            case ONLY_PLAYER -> sessionRepository.findAllByPlayerId(userId);
            case ONLY_DM -> sessionRepository.findAllByDmId(userId);
            default -> Uni.combine().all()
                .unis(sessionRepository.findAllByPlayerId(userId), sessionRepository.findAllByDmId(userId))
                .asTuple().onItem().transform(results -> {
                    Map<UUID, SessionEntity> combine = new HashMap<>();
                    results.getItem1().forEach(s -> combine.put(s.getSessionId(), s));
                    results.getItem2().forEach(s -> combine.put(s.getSessionId(), s));
                    return combine.values().stream().toList();
                });
        };
        return sessions
                .onItem()
                .transform(sessionEntities ->
                     sessionEntities.stream().<BasicSessionListDTO>map(sessionEntity ->
                         BasicSessionListDTO.builder()
                                 .uuid(sessionEntity.getSessionId())
                                 .description(sessionEntity.getSessionName())
                                 .dmed(sessionEntity.getDmId().equals(userId))
                                 .build()
                     ).toList()
                );
    }



    private Uni<SessionDetailsDTO> findSessionByIdV1(UUID sessionId) {
        return Uni.combine().all().unis(sessionRepository.findOneById(sessionId), characterSheetRepository.findAllBySessionId(sessionId))
            .asTuple()
            .onItem()
            .transform(records -> SessionDetailsDTO.builder()
                .sessionName(records.getItem1().getSessionName())
                .id(records.getItem1().getDmId())
                .characterSheets(
                        records.getItem2().stream().<BasicListDTO>map(sheet -> BasicListDTO.builder()
                            .uuid(sheet.getId())
                            .description(sheet.getCharacterName())
                            .build()
                        ).toList()
                )
                .build()
        );
    }
}
