package com.hbsites.rpgtracker.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CharacterSheetDetailsDTO {
    private UUID id;
    private String characterName;
}
