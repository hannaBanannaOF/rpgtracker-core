package com.hbsites.rpgtracker.infrastructure.provider;

import com.hbsites.commons.infrastructure.config.ProviderManager;
import com.hbsites.rpgtracker.domain.model.RpgTrackerUserInfo;
import com.hbsites.rpgtracker.infrastructure.repository.CharacterSheetRepositoryImpl;
import com.hbsites.rpgtracker.infrastructure.repository.interfaces.SessionRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.UUID;

@ApplicationScoped
public class ProviderManagerImpl implements ProviderManager {

    @Inject
    private RpgTrackerUserInfoProvider provider;

    @Inject
    CharacterSheetRepositoryImpl characterSheetRepository;

    @Inject
    SessionRepository sessionRepository;

    @Inject
    JsonWebToken token;

    @Override
    public Uni<Void> populate() {
        UUID user = UUID.fromString(token.getSubject());
        return Uni.combine().all().unis(
                sessionRepository.findAllSessionIdsByDmId(user),
                characterSheetRepository.findAllCharacterSheetIdByPlayerId(user)
        ).asTuple().onItem().transform(objects -> {
            provider.setProviderValue(RpgTrackerUserInfo.builder()
                    .userSheets(objects.getItem2())
                    .userDmedSessionsSheet(objects.getItem1())
                    .build()
                );
            return null;
        });
    }
}
