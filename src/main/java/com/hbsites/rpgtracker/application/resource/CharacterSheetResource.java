package com.hbsites.rpgtracker.application.resource;

import com.hbsites.commons.domain.params.GetOneParams;
import com.hbsites.rpgtracker.application.service.adapters.CharacterSheetAdapter;
import com.hbsites.rpgtracker.domain.model.CharacterSheetDetailsItem;
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
public class CharacterSheetResource {

    @Inject
    CharacterSheetAdapter characterSheetAdapter;

    @GET
    @Path("/{slug}")
    public Uni<CharacterSheetDetailsItem> findSheetBySlug(@BeanParam @PermittedSheet GetOneParams params) {
        return characterSheetAdapter.findSheetBySlug(params);
    }
}
