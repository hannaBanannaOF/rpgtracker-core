package com.hbsites.rpgtracker.application.resource;

import com.hbsites.rpgtracker.application.service.CharacterSheetService;
import com.hbsites.rpgtracker.domain.dto.CharacterSheetDetailsDTO;
import com.hbsites.rpgtracker.domain.params.CharacterSheetParams;
import com.hbsites.rpgtracker.infrastructure.interceptors.PermittedSheet;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/core/api/sheets")
@RolesAllowed("user")
public class CharacterSheetResource {

    @Inject
    CharacterSheetService characterSheetService;

    @GET
    @Path("/{sheetId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<CharacterSheetDetailsDTO> getDMedSessions(@BeanParam @PermittedSheet CharacterSheetParams params) {
        return characterSheetService.findSheetById(params);
    }
}
