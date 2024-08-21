package com.hbsites.rpgtracker.application.service;

import com.hbsites.commons.domain.dto.BasicListDTO;
import com.hbsites.commons.domain.params.DefaultParams;
import com.hbsites.commons.rpgtracker.infrastructure.entity.CharacterSheetEntity;
import com.hbsites.rpgtracker.infrastructure.repository.CharacterSheetRepository;
import io.quarkus.hibernate.reactive.panache.common.WithSessionOnDemand;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
@WithSessionOnDemand
public class CharacterSheetService {

    @Inject
    CharacterSheetRepository characterSheetRepository;

    @Inject
    JsonWebToken token;

    public Uni<List<BasicListDTO>> getCurrentUserSheets(DefaultParams params) {
        return getCurrentUserSheetsV1();
    }

    private Uni<List<BasicListDTO>> getCurrentUserSheetsV1() {
        return characterSheetRepository.list("playerId", UUID.fromString(token.getSubject()))
                .onItem()
                .transform(characterSheetEntities ->
                    characterSheetEntities.stream().<BasicListDTO>map(characterSheetEntity ->
                        BasicListDTO.builder().uuid(characterSheetEntity.getId()).description(characterSheetEntity.getCharacterName()).build()
                    ).toList()
                );
    }
}
