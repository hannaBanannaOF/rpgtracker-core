package com.hbsites.rpgtracker.application.service.v1;

import com.hbsites.commons.domain.params.DefaultParams;
import com.hbsites.rpgtracker.application.service.interfaces.CharacterSheetService;
import com.hbsites.rpgtracker.domain.model.CharacterSheetListItem;
import com.hbsites.rpgtracker.domain.model.CharacterSheetDetailsItem;
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

    public Uni<List<CharacterSheetListItem>> getCurrentUserSheets(DefaultParams params) {
        return characterSheetRepository.findAllByPlayerId(UUID.fromString(token.getSubject()))
                .onItem()
                .transform(characterSheetEntities ->
                        characterSheetEntities.stream().<CharacterSheetListItem>map(characterSheetEntity ->
                                new CharacterSheetListItem(characterSheetEntity.slug(), characterSheetEntity.characterName(), characterSheetEntity.trpgSystem())
                        ).toList()
                );
    }

    public Uni<CharacterSheetDetailsItem> findSheetBySlug(CharacterSheetParams params) {
        return characterSheetRepository.findOneBySlug(params.getSlug()).onItem().transform(res ->
                new CharacterSheetDetailsItem(res.slug(), res.characterName())
        );
    }
}
