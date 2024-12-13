package com.hbsites.rpgtracker.application.service.interfaces;

import com.hbsites.rpgtracker.domain.model.SessionListItem;
import com.hbsites.rpgtracker.domain.model.NextSessionItem;
import com.hbsites.rpgtracker.domain.model.SessionDetailsItem;
import com.hbsites.rpgtracker.domain.params.SessionCalendarParams;
import com.hbsites.rpgtracker.domain.params.SessionListParams;
import com.hbsites.rpgtracker.domain.params.SessionParams;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface SessionService {

    Uni<List<SessionListItem>> getMySessions(SessionListParams params);

    Uni<SessionDetailsItem> findSessionBySlug(SessionParams params);

    Uni<List<NextSessionItem>> getMySessionCalendar(SessionCalendarParams params);

    public Uni<NextSessionItem> getMyNextSession(SessionListParams params);
}
