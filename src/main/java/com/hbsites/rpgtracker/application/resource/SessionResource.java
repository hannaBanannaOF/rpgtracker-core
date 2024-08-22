package com.hbsites.rpgtracker.application.resource;

import com.hbsites.rpgtracker.application.service.CharacterSheetService;
import com.hbsites.rpgtracker.application.service.SessionService;
import com.hbsites.rpgtracker.domain.dto.CharacterSheetDetailsDTO;
import com.hbsites.rpgtracker.domain.dto.SessionDetailsDTO;
import com.hbsites.rpgtracker.domain.params.CharacterSheetParams;
import com.hbsites.rpgtracker.domain.params.SessionParams;
import com.hbsites.rpgtracker.infrastructure.interceptors.PermittedSession;
import com.hbsites.rpgtracker.infrastructure.interceptors.PermittedSheet;
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
    SessionService sessionService;

    @GET
    @Path("/{sessionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<SessionDetailsDTO> getDMedSessions(@BeanParam @PermittedSession SessionParams params) {
        return sessionService.findSessionById(params);
    }
}
