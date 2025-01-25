package com.hbsites.rpgtracker.application.service.v1;

import com.hbsites.commons.domain.model.BasicListItem;
import com.hbsites.commons.domain.params.GetOneParams;
import com.hbsites.rpgtracker.application.service.interfaces.SessionService;
import com.hbsites.rpgtracker.domain.model.NextSessionItem;
import com.hbsites.rpgtracker.domain.model.ScheduleItem;
import com.hbsites.rpgtracker.domain.model.SessionDetailsItem;
import com.hbsites.rpgtracker.domain.model.SessionListItem;
import com.hbsites.rpgtracker.domain.params.SessionCalendarParams;
import com.hbsites.rpgtracker.domain.params.SessionListParams;
import com.hbsites.rpgtracker.infrastructure.repository.interfaces.CharacterSheetRepository;
import com.hbsites.rpgtracker.infrastructure.repository.interfaces.SessionRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
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
    public Uni<List<SessionListItem>> getMySessions(SessionListParams params) {
        UUID userId = UUID.fromString(token.getSubject());
        return sessionRepository.findAllByPlayerIdOrDmId(userId, params.getDmedFilter())
                .onItem()
                .transform(sessionEntities ->
                        sessionEntities.stream().<SessionListItem>map(sessionEntity ->
                                new SessionListItem(sessionEntity.slug(), sessionEntity.sessionName(), sessionEntity.dmId().equals(userId), sessionEntity.trpgSystem())
                        ).toList()
                );
    }

    @Override
    public Uni<SessionDetailsItem> findSessionBySlug(GetOneParams params) {
        return sessionRepository.findOneBySlug(params.getSlug())
                .onItem()
                .transformToUni(session -> {
                    if (session == null) {
                        throw new NotFoundException();
                    }
                    boolean dmed = session.dmId().equals(UUID.fromString(token.getSubject()));
                    if (dmed) {
                        return characterSheetRepository.findAllBySessionSlug(params.getSlug()).onItem()
                                .transform(sheets -> new SessionDetailsItem(session.slug(), session.sessionName(), sheets.stream()
                                        .map(sheet ->
                                                new BasicListItem<>(sheet.slug(), sheet.characterName())
                                        ).toList(), dmed, session.id(), session.trpgSystem(), session.inPlay())
                                );
                    }
                    return characterSheetRepository.findAllBySessionSlugAndPlayerId(params.getSlug(), UUID.fromString(token.getSubject())).onItem()
                            .transform(sheets -> new SessionDetailsItem(session.slug(), session.sessionName(), sheets.stream()
                                .map(sheet ->
                                        new BasicListItem<>(sheet.slug(), sheet.characterName())
                                ).toList(), dmed, session.id(), session.trpgSystem(), session.inPlay())
                            );
                });
    }

    @Override
    public Uni<List<NextSessionItem>> getMySessionCalendar(SessionCalendarParams params) {
        YearMonth yearMonth = YearMonth.of(params.getYear(), params.getMonth());
        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59,59);
        return sessionRepository.findAllByDmIdOrPlayerIdWithDateRange(UUID.fromString(token.getSubject()), startOfMonth, endOfMonth, false, params.getDmedFilter());
    }

    @Override
    public Uni<NextSessionItem> getMyNextSession(SessionListParams params) {
        return sessionRepository.findAllByDmIdOrPlayerIdWithDateRange(UUID.fromString(token.getSubject()), LocalDateTime.now(), null, true, params.getDmedFilter())
                .onItem()
                .transform(res -> {
                    if (res.isEmpty()) {
                        return null;
                    }
                    return res.getFirst();
                });
    }

    @Override
    public Uni<SessionDetailsItem> setSessionInPlay(GetOneParams params) {
        return sessionRepository.findOneBySlug(params.getSlug()).onItem().transformToUni(sessionEntity -> {
            boolean dmed = sessionEntity.dmId().equals(UUID.fromString(token.getSubject()));
            if (!dmed) {
                throw new ForbiddenException();
            }
            return sessionRepository.updateInPlay(sessionEntity).onItem().transformToUni(session -> characterSheetRepository.findAllBySessionSlug(params.getSlug()).onItem()
                    .transform(sheets -> new SessionDetailsItem(session.slug(), session.sessionName(), sheets.stream()
                            .map(sheet ->
                                    new BasicListItem<>(sheet.slug(), sheet.characterName())
                            ).toList(), dmed, session.id(), session.trpgSystem(), session.inPlay())
                    ));
        });
    }

    @Override
    public Uni<Void> scheduleSession(GetOneParams params, ScheduleItem schedule) {
        if (schedule == null || schedule.dateTime() == null || LocalDateTime.now().isAfter(schedule.dateTime())) {
            throw new BadRequestException();
        }
        return sessionRepository.findOneBySlug(params.getSlug()).onItem().transformToUni(session -> {
            if (session == null) {
                throw new NotFoundException();
            }
            if (!session.dmId().equals(UUID.fromString(token.getSubject()))) {
                throw new ForbiddenException();
            }
            return sessionRepository.scheduleSession(session, schedule.dateTime());
        });
    }

    @Override
    public Uni<Void> deleteSchedule(GetOneParams params, Integer id) {
        return findSessionBySlug(params).onItem().transformToUni(session -> {
            if (!session.dmed()) {
                throw new ForbiddenException();
            }
            return sessionRepository.deleteSchedule(id);
        }
        );
    }
}
