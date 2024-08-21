package com.hbsites.rpgtracker.application.service;

import com.hbsites.commons.domain.dto.BasicListDTO;
import com.hbsites.commons.domain.enumeration.EMicroservice;
import com.hbsites.commons.infrastructure.messages.incoming.UserInfoRequestMessage;
import com.hbsites.commons.rpgtracker.infrastructure.entity.CharacterSheetEntity;
import com.hbsites.commons.rpgtracker.infrastructure.entity.SessionEntity;
import com.hbsites.rpgtracker.domain.dto.BasicSessionListDTO;
import com.hbsites.rpgtracker.domain.params.SessionParams;
import com.hbsites.rpgtracker.infrastructure.repository.SessionRepository;
import io.quarkus.hibernate.reactive.panache.common.WithSessionOnDemand;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.smallrye.reactive.messaging.rabbitmq.OutgoingRabbitMQMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@WithSessionOnDemand
public class SessionService {

    @Inject
    SessionRepository sessionRepository;

    @Inject
    JsonWebToken token;

    @Inject
    @Channel("user-requests")
    Emitter<UserInfoRequestMessage> emitter;

    public Uni<List<BasicSessionListDTO>> getMySessions(SessionParams params) {
        return getMySessionsV1(params);
    }

    private Uni<List<BasicSessionListDTO>> getMySessionsV1(SessionParams params) {
        UUID userId = UUID.fromString(token.getSubject());
        Uni<List<SessionEntity>> sessions = switch (params.getDmedFilter()) {
            case ONLY_PLAYER -> sessionRepository.findAllByPlayerId(userId);
            case ONLY_DM -> sessionRepository.findAllByDmId(userId);
            default -> sessionRepository.findAllByPlayerIdOrPlayerId(userId);
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
}
