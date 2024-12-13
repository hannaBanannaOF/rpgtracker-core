package com.hbsites.rpgtracker.application.service.adapters;

import com.hbsites.commons.domain.service.VersionedService;
import com.hbsites.rpgtracker.application.service.interfaces.SessionService;
import com.hbsites.rpgtracker.application.service.v1.SessionServiceV1;
import com.hbsites.rpgtracker.domain.model.SessionListItem;
import com.hbsites.rpgtracker.domain.model.NextSessionItem;
import com.hbsites.rpgtracker.domain.model.SessionDetailsItem;
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
    public Uni<List<SessionListItem>> getMySessions(SessionListParams params) {
        return getServiceByApiVersion(params.getApiVersion()).getMySessions(params);
    }

    @Override
    public Uni<SessionDetailsItem> findSessionBySlug(SessionParams params) {
        return getServiceByApiVersion(params.getApiVersion()).findSessionBySlug(params);
    }

    @Override
    public Uni<List<NextSessionItem>> getMySessionCalendar(SessionCalendarParams params) {
        return getServiceByApiVersion(params.getApiVersion()).getMySessionCalendar(params);
    }

    @Override
    public Uni<NextSessionItem> getMyNextSession(SessionListParams params) {
        return getServiceByApiVersion(params.getApiVersion()).getMyNextSession(params);
    }

    @Override
    protected SessionService getServiceByApiVersion(int apiVersion) {
        return switch(apiVersion){
            default -> sessionServiceV1;
        };
    }
}
