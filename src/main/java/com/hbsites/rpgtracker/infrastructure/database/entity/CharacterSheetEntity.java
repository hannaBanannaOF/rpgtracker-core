package com.hbsites.rpgtracker.infrastructure.database.entity;

import com.hbsites.commons.rpgtracker.domain.enumeration.ETRPGSystem;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;
import org.seasar.doma.jdbc.entity.NamingType;

import java.util.UUID;

@Entity(metamodel = @Metamodel, naming = NamingType.SNAKE_LOWER_CASE)
@Table(name = "character_sheet")
public record CharacterSheetEntity(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) Integer id,
                                   String characterName,
                                   UUID playerId,
                                   ETRPGSystem trpgSystem,
                                   Integer sessionId,
                                   String slug) {}
