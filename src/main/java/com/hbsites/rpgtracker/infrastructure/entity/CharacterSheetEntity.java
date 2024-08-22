package com.hbsites.rpgtracker.infrastructure.entity;

import com.hbsites.commons.infrastructure.database.entity.BaseEntity;
import com.hbsites.commons.infrastructure.database.entitylistener.BaseEntityListener;
import com.hbsites.commons.rpgtracker.domain.enumeration.ETRPGSystem;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;

import java.util.UUID;

@Entity(listener = BaseEntityListener.class, metamodel = @Metamodel)
@Table(name = "character_sheet")
@Getter
@Setter
public class CharacterSheetEntity extends BaseEntity {

    @Column(name = "id")
    @Id
    private UUID id;

    @Column(name = "character_name")
    private String characterName;

    @Column(name = "player_id")
    private UUID playerId;

    @Column(name = "trpg_system")
    private ETRPGSystem trpgSystem;

    @Column(name= "session_id")
    private UUID sessionId;

}
