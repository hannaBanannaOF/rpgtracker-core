package com.hbsites.rpgtracker.domain.dto;

import com.hbsites.commons.domain.dto.BasicListDTO;
import com.hbsites.commons.rpgtracker.domain.enumeration.ETRPGSystem;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class BasicSessionListDTO extends BasicListDTO {
    private final boolean dmed;
    private final ETRPGSystem system;
}
