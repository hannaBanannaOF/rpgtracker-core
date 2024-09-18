package com.hbsites.rpgtracker.application.resource;

import com.hbsites.rpgtracker.application.service.adapters.CharacterSheetAdapter;
import com.hbsites.rpgtracker.application.service.v1.CharacterSheetServiceV1;
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
    CharacterSheetAdapter characterSheetAdapter;

    @GET
    @Path("/{sheetId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<CharacterSheetDetailsDTO> getDMedSessions(@BeanParam @PermittedSheet CharacterSheetParams params) {
        return characterSheetAdapter.findSheetById(params);
    }
}
