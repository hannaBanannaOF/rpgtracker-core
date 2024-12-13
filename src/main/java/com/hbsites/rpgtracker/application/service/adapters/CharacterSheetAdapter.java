package com.hbsites.rpgtracker.application.service.adapters;

import com.hbsites.commons.domain.params.DefaultParams;
import com.hbsites.commons.domain.service.VersionedService;
import com.hbsites.rpgtracker.application.service.interfaces.CharacterSheetService;
import com.hbsites.rpgtracker.application.service.v1.CharacterSheetServiceV1;
import com.hbsites.rpgtracker.domain.model.CharacterSheetListItem;
import com.hbsites.rpgtracker.domain.model.CharacterSheetDetailsItem;
import com.hbsites.rpgtracker.domain.params.CharacterSheetParams;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class CharacterSheetAdapter extends VersionedService<CharacterSheetService> implements CharacterSheetService {

    @Inject
    CharacterSheetServiceV1 characterSheetServiceV1;

    @Override
    protected CharacterSheetService getServiceByApiVersion(int apiVersion) {
        return switch(apiVersion){
            default -> characterSheetServiceV1;
        };
    }

    @Override
    public Uni<List<CharacterSheetListItem>> getCurrentUserSheets(DefaultParams params) {
        return getServiceByApiVersion(params.getApiVersion()).getCurrentUserSheets(params);
    }

    @Override
    public Uni<CharacterSheetDetailsItem> findSheetBySlug(CharacterSheetParams params) {
        return getServiceByApiVersion(params.getApiVersion()).findSheetBySlug(params);
    }
}
