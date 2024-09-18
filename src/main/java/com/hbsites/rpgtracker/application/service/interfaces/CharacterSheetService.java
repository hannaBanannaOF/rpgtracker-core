package com.hbsites.rpgtracker.application.service.interfaces;

import com.hbsites.commons.domain.params.DefaultParams;
import com.hbsites.rpgtracker.domain.dto.BasicCharacterSheetListDTO;
import com.hbsites.rpgtracker.domain.dto.CharacterSheetDetailsDTO;
import com.hbsites.rpgtracker.domain.params.CharacterSheetParams;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface CharacterSheetService {

    Uni<List<BasicCharacterSheetListDTO>> getCurrentUserSheets(DefaultParams params);

    Uni<CharacterSheetDetailsDTO> findSheetById(CharacterSheetParams params);
}
