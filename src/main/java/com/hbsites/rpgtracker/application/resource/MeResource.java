package com.hbsites.rpgtracker.application.resource;

import com.hbsites.commons.domain.params.DefaultParams;
import com.hbsites.rpgtracker.application.service.adapters.CharacterSheetAdapter;
import com.hbsites.rpgtracker.application.service.adapters.SessionServiceAdapter;
import com.hbsites.rpgtracker.domain.dto.BasicCharacterSheetListDTO;
import com.hbsites.rpgtracker.domain.dto.BasicSessionListDTO;
import com.hbsites.rpgtracker.domain.dto.NextSessionsDTO;
import com.hbsites.rpgtracker.domain.params.SessionCalendarParams;
import com.hbsites.rpgtracker.domain.params.SessionListParams;
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
    SessionServiceAdapter sessionServiceAdapter;

    @Inject
    CharacterSheetAdapter characterSheetAdapter;

    @GET
    @Path("/sessions")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<BasicSessionListDTO>> getDMedSessions(@BeanParam SessionListParams params) {
        return sessionServiceAdapter.getMySessions(params);
    }

    @GET
    @Path("/sessions/calendar")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<NextSessionsDTO>> getMyNextSessions(@BeanParam SessionCalendarParams params) {
        return sessionServiceAdapter.getMySessionCalendar(params);
    }

    @GET
    @Path("/sessions/next")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<NextSessionsDTO> getMyNextSessions(@BeanParam SessionListParams params) {
        return sessionServiceAdapter.getMyNextSession(params);
    }

    @GET
    @Path("/sheets")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<BasicCharacterSheetListDTO>> getMyCharacterSheets(@BeanParam DefaultParams params) {
        return characterSheetAdapter.getCurrentUserSheets(params);
    }

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> greet(){
        return Uni.createFrom().item(() -> "Hello from service 1");
    }
}
