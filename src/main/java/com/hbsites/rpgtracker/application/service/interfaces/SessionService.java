package com.hbsites.rpgtracker.application.service.interfaces;

import com.hbsites.rpgtracker.domain.dto.BasicSessionListDTO;
import com.hbsites.rpgtracker.domain.dto.NextSessionsDTO;
import com.hbsites.rpgtracker.domain.dto.SessionDetailsDTO;
import com.hbsites.rpgtracker.domain.params.SessionCalendarParams;
import com.hbsites.rpgtracker.domain.params.SessionListParams;
import com.hbsites.rpgtracker.domain.params.SessionParams;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface SessionService {

    Uni<List<BasicSessionListDTO>> getMySessions(SessionListParams params);

    Uni<SessionDetailsDTO> findSessionById(SessionParams params);

    Uni<List<NextSessionsDTO>> getMySessionCalendar(SessionCalendarParams params);

    public Uni<NextSessionsDTO> getMyNextSession(SessionListParams params);
}
