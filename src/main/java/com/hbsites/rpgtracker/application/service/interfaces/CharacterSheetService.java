package com.hbsites.rpgtracker.application.service.interfaces;

import com.hbsites.commons.domain.params.DefaultParams;
import com.hbsites.rpgtracker.domain.model.CharacterSheetListItem;
import com.hbsites.rpgtracker.domain.model.CharacterSheetDetailsItem;
import com.hbsites.rpgtracker.domain.params.CharacterSheetParams;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface CharacterSheetService {

    Uni<List<CharacterSheetListItem>> getCurrentUserSheets(DefaultParams params);

    Uni<CharacterSheetDetailsItem> findSheetBySlug(CharacterSheetParams params);
}
