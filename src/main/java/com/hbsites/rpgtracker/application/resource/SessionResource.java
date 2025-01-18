package com.hbsites.rpgtracker.application.resource;

import com.hbsites.commons.domain.params.GetOneParams;
import com.hbsites.rpgtracker.application.service.adapters.SessionServiceAdapter;
import com.hbsites.rpgtracker.domain.model.ScheduleItem;
import com.hbsites.rpgtracker.domain.model.SessionDetailsItem;
import com.hbsites.rpgtracker.infrastructure.interceptors.PermittedSession;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/core/api/sessions")
public class SessionResource {
    @Inject
    SessionServiceAdapter sessionServiceAdapter;

    @GET
    @Path("/{slug}")
    public Uni<SessionDetailsItem> getSessionBySlug(@BeanParam @PermittedSession GetOneParams params) {
        return sessionServiceAdapter.findSessionBySlug(params);
    }

    @PUT
    @Path("/{slug}/toggle-in-play")
    public Uni<SessionDetailsItem> setSessionInPlayBySlug(@BeanParam @PermittedSession GetOneParams params) {
        return sessionServiceAdapter.setSessionInPlay(params);
    }

    @POST
    @Path("/{slug}/schedule")
    public Uni<Void> scheduleSession(@BeanParam @PermittedSession GetOneParams params, ScheduleItem schedule) {
        return sessionServiceAdapter.scheduleSession(params, schedule);
    }
}
