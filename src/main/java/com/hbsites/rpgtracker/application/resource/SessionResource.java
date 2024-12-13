package com.hbsites.rpgtracker.application.resource;

import com.hbsites.rpgtracker.application.service.adapters.SessionServiceAdapter;
import com.hbsites.rpgtracker.domain.model.SessionDetailsItem;
import com.hbsites.rpgtracker.domain.params.SessionParams;
import com.hbsites.rpgtracker.infrastructure.interceptors.PermittedSession;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/core/api/sessions")
@RolesAllowed("user")
public class SessionResource {
    @Inject
    SessionServiceAdapter sessionServiceAdapter;

    @GET
    @Path("/{slug}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<SessionDetailsItem> getSessionBySlug(@BeanParam @PermittedSession SessionParams params) {
        return sessionServiceAdapter.findSessionBySlug(params);
    }
}
