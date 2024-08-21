package com.hbsites.rpgtracker.infrastructure.repository;

import com.hbsites.commons.rpgtracker.infrastructure.entity.CharacterSheetEntity;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CharacterSheetRepository implements PanacheRepository<CharacterSheetEntity> {
}
