package com.hbsites.rpgtracker.application.resource;

import com.hbsites.commons.domain.params.DefaultParams;
import com.hbsites.rpgtracker.application.service.adapters.CharacterSheetAdapter;
import com.hbsites.rpgtracker.application.service.adapters.SessionServiceAdapter;
import com.hbsites.rpgtracker.domain.model.CharacterSheetListItem;
import com.hbsites.rpgtracker.domain.model.SessionListItem;
import com.hbsites.rpgtracker.domain.model.NextSessionItem;
import com.hbsites.rpgtracker.domain.params.SessionCalendarParams;
import com.hbsites.rpgtracker.domain.params.SessionListParams;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import java.util.List;

@Path("/core/api/me")
public class MeResource {

    @Inject
    SessionServiceAdapter sessionServiceAdapter;

    @Inject
    CharacterSheetAdapter characterSheetAdapter;

    @GET
    @Path("/sessions")
    public Uni<List<SessionListItem>> getDMedSessions(@BeanParam SessionListParams params) {
        return sessionServiceAdapter.getMySessions(params);
    }

    @GET
    @Path("/sessions/calendar")
    public Uni<List<NextSessionItem>> getMyNextSessions(@BeanParam SessionCalendarParams params) {
        return sessionServiceAdapter.getMySessionCalendar(params);
    }

    @GET
    @Path("/sessions/next")
    public Uni<NextSessionItem> getMyNextSessions(@BeanParam SessionListParams params) {
        return sessionServiceAdapter.getMyNextSession(params);
    }

    @GET
    @Path("/sheets")
    public Uni<List<CharacterSheetListItem>> getMyCharacterSheets(@BeanParam DefaultParams params) {
        return characterSheetAdapter.getCurrentUserSheets(params);
    }
}
