package com.hbsites.rpgtracker.domain.params;

import com.hbsites.commons.domain.params.DefaultParams;
import jakarta.ws.rs.PathParam;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CharacterSheetParams extends DefaultParams {

    @PathParam("sheetId")
    UUID sheetId;
}
