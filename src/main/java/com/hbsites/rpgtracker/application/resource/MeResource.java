package com.hbsites.rpgtracker.application.resource;

import com.hbsites.commons.domain.dto.BasicListDTO;
import com.hbsites.commons.domain.params.DefaultParams;
import com.hbsites.rpgtracker.application.service.CharacterSheetService;
import com.hbsites.rpgtracker.application.service.SessionService;
import com.hbsites.rpgtracker.domain.dto.BasicSessionListDTO;
import com.hbsites.rpgtracker.domain.params.SessionParams;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/core/api/me")
@RolesAllowed("user")
public class MeResource {

    @Inject
    SessionService sessionService;

    @Inject
    CharacterSheetService characterSheetService;

    @GET
    @Path("/sessions")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<BasicSessionListDTO>> getDMedSessions(@BeanParam SessionParams params) {
        return sessionService.getMySessions(params);
    }

    @GET
    @Path("/sheets")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<BasicListDTO>> getMyCharacterSheets(@BeanParam DefaultParams params) {
        return characterSheetService.getCurrentUserSheets(params);
    }
}
