package com.hbsites.rpgtracker.application.service.v1;

import com.hbsites.commons.domain.dto.BasicListDTO;
import com.hbsites.rpgtracker.application.service.interfaces.SessionService;
import com.hbsites.rpgtracker.domain.dto.BasicSessionListDTO;
import com.hbsites.rpgtracker.domain.dto.NextSessionsDTO;
import com.hbsites.rpgtracker.domain.dto.SessionDetailsDTO;
import com.hbsites.rpgtracker.domain.params.SessionCalendarParams;
import com.hbsites.rpgtracker.domain.params.SessionListParams;
import com.hbsites.rpgtracker.domain.params.SessionParams;
import com.hbsites.rpgtracker.infrastructure.repository.interfaces.CharacterSheetRepository;
import com.hbsites.rpgtracker.infrastructure.repository.interfaces.SessionRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class SessionServiceV1 implements SessionService {

    @Inject
    SessionRepository sessionRepository;

    @Inject
    CharacterSheetRepository characterSheetRepository;

    @Inject
    JsonWebToken token;

    @Override
    public Uni<List<BasicSessionListDTO>> getMySessions(SessionListParams params) {
        UUID userId = UUID.fromString(token.getSubject());
        return sessionRepository.findAllByPlayerIdOrDmId(userId, params.getDmedFilter())
                .onItem()
                .transform(sessionEntities ->
                        sessionEntities.stream().<BasicSessionListDTO>map(sessionEntity -> BasicSessionListDTO.builder()
                                .uuid(sessionEntity.getSessionId())
                                .description(sessionEntity.getSessionName())
                                .dmed(sessionEntity.getDmId().equals(userId))
                                .system(sessionEntity.getTrpgSystem())
                                .build()
                        ).toList()
                );
    }

    @Override
    public Uni<SessionDetailsDTO> findSessionById(SessionParams params) {
        return Uni.combine().all().unis(sessionRepository.findOneById(params.getSessionId()), characterSheetRepository.findAllBySessionId(params.getSessionId()))
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

    @Override
    public Uni<List<NextSessionsDTO>> getMySessionCalendar(SessionCalendarParams params) {
        YearMonth yearMonth = YearMonth.from(LocalDateTime.now()).withMonth(params.getMonth());
        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59,59);
        return sessionRepository.findAllByDmIdOrPlayerIdWithDateRange(UUID.fromString(token.getSubject()), startOfMonth, endOfMonth, false, params.getDmedFilter());
    }

    @Override
    public Uni<NextSessionsDTO> getMyNextSession(SessionListParams params) {
        return sessionRepository.findAllByDmIdOrPlayerIdWithDateRange(UUID.fromString(token.getSubject()), LocalDateTime.now(), null, true, params.getDmedFilter())
                .onItem()
                .transform(res -> {
                    if (res.isEmpty()) {
                        return null;
                    }
                    return res.getFirst();
                });
    }
}
