package com.hbsites.rpgtracker.application.service.interfaces;

import com.hbsites.commons.domain.params.GetOneParams;
import com.hbsites.rpgtracker.domain.model.ScheduleItem;
import com.hbsites.rpgtracker.domain.model.SessionListItem;
import com.hbsites.rpgtracker.domain.model.NextSessionItem;
import com.hbsites.rpgtracker.domain.model.SessionDetailsItem;
import com.hbsites.rpgtracker.domain.params.SessionCalendarParams;
import com.hbsites.rpgtracker.domain.params.SessionListParams;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface SessionService {

    Uni<List<SessionListItem>> getMySessions(SessionListParams params);

    Uni<SessionDetailsItem> findSessionBySlug(GetOneParams params);

    Uni<List<NextSessionItem>> getMySessionCalendar(SessionCalendarParams params);

    Uni<NextSessionItem> getMyNextSession(SessionListParams params);

    Uni<SessionDetailsItem> setSessionInPlay(GetOneParams params);

    Uni<Void> scheduleSession(GetOneParams params, ScheduleItem schedule);
}
