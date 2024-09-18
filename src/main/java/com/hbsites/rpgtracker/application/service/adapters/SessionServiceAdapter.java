package com.hbsites.rpgtracker.application.service.adapters;

import com.hbsites.commons.domain.service.VersionedService;
import com.hbsites.rpgtracker.application.service.interfaces.SessionService;
import com.hbsites.rpgtracker.application.service.v1.SessionServiceV1;
import com.hbsites.rpgtracker.domain.dto.BasicSessionListDTO;
import com.hbsites.rpgtracker.domain.dto.NextSessionsDTO;
import com.hbsites.rpgtracker.domain.dto.SessionDetailsDTO;
import com.hbsites.rpgtracker.domain.params.SessionCalendarParams;
import com.hbsites.rpgtracker.domain.params.SessionListParams;
import com.hbsites.rpgtracker.domain.params.SessionParams;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class SessionServiceAdapter extends VersionedService<SessionService> implements SessionService {

    @Inject
    SessionServiceV1 sessionServiceV1;

    @Override
    public Uni<List<BasicSessionListDTO>> getMySessions(SessionListParams params) {
        return getServiceByApiVersion(params.getApiVersion()).getMySessions(params);
    }

    @Override
    public Uni<SessionDetailsDTO> findSessionById(SessionParams params) {
        return null;
    }

    @Override
    public Uni<List<NextSessionsDTO>> getMySessionCalendar(SessionCalendarParams params) {
        return null;
    }

    @Override
    public Uni<NextSessionsDTO> getMyNextSession(SessionListParams params) {
        return null;
    }

    @Override
    protected SessionService getServiceByApiVersion(int apiVersion) {
        return switch(apiVersion){
            default -> sessionServiceV1;
        };
    }
}
