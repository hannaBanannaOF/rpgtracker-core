package com.hbsites.rpgtracker.domain.dto;

import com.hbsites.commons.domain.dto.BasicListDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class SessionDetailsDTO {
    private UUID id;
    private String sessionName;
    private List<BasicListDTO> characterSheets;
}
