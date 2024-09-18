package com.hbsites.rpgtracker.application.service.v1;

import com.hbsites.commons.domain.params.DefaultParams;
import com.hbsites.rpgtracker.application.service.interfaces.CharacterSheetService;
import com.hbsites.rpgtracker.domain.dto.BasicCharacterSheetListDTO;
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
public class CharacterSheetServiceV1 implements CharacterSheetService {

    @Inject
    CharacterSheetRepositoryImpl characterSheetRepository;

    @Inject
    JsonWebToken token;

    public Uni<List<BasicCharacterSheetListDTO>> getCurrentUserSheets(DefaultParams params) {
        return characterSheetRepository.findAllByPlayerId(UUID.fromString(token.getSubject()))
                .onItem()
                .transform(characterSheetEntities ->
                        characterSheetEntities.stream().<BasicCharacterSheetListDTO>map(characterSheetEntity ->
                                BasicCharacterSheetListDTO.builder()
                                        .uuid(characterSheetEntity.getId())
                                        .description(characterSheetEntity.getCharacterName())
                                        .system(characterSheetEntity.getTrpgSystem())
                                        .build()
                        ).toList()
                );
    }

    public Uni<CharacterSheetDetailsDTO> findSheetById(CharacterSheetParams params) {
        return characterSheetRepository.findOne(params.getSheetId()).onItem().transform(res -> CharacterSheetDetailsDTO
                .builder()
                .id(res.getId())
                .characterName(res.getCharacterName())
                .build()
        );
    }
}
