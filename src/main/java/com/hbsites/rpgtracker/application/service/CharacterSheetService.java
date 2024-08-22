package com.hbsites.rpgtracker.application.service;

import com.hbsites.commons.domain.dto.BasicListDTO;
import com.hbsites.commons.domain.params.DefaultParams;
import com.hbsites.rpgtracker.domain.dto.CharacterSheetDetailsDTO;
import com.hbsites.rpgtracker.domain.params.CharacterSheetParams;
import com.hbsites.rpgtracker.infrastructure.repository.CharacterSheetRepositoryImpl;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CharacterSheetService {

    @Inject
    CharacterSheetRepositoryImpl characterSheetRepository;

    @Inject
    JsonWebToken token;

    public Uni<List<BasicListDTO>> getCurrentUserSheets(DefaultParams params) {
        return getCurrentUserSheetsV1();
    }

    public Uni<CharacterSheetDetailsDTO> findSheetById(CharacterSheetParams params) {
        return findSheetByIdV1(params.getSheetId());
    }

    private Uni<List<BasicListDTO>> getCurrentUserSheetsV1() {
        return characterSheetRepository.findAllByPlayerId(UUID.fromString(token.getSubject()))
            .onItem()
            .transform(characterSheetEntities ->
                characterSheetEntities.stream().<BasicListDTO>map(characterSheetEntity ->
                    BasicListDTO.builder().uuid(characterSheetEntity.getId()).description(characterSheetEntity.getCharacterName()).build()
                ).toList()
            );
    }

    private Uni<CharacterSheetDetailsDTO> findSheetByIdV1(UUID sheetId) {
        return characterSheetRepository.findOne(sheetId).onItem().transform(res -> CharacterSheetDetailsDTO
            .builder()
            .id(res.getId())
            .characterName(res.getCharacterName())
            .build()
        );
    }
}
