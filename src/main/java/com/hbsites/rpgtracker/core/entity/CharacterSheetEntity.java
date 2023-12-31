package com.hbsites.rpgtracker.core.entity;

import com.hbsites.hbsitescommons.commons.entity.RabbitBaseEntity;
import com.hbsites.hbsitescommons.rpgtracker.enumeration.ETRPGSystem;
import com.hbsites.hbsitescommons.commons.interfaces.EventProducerInterface;
import com.hbsites.hbsitescommons.rpgtracker.dto.CharacterSheetListingDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.apache.commons.lang3.NotImplementedException;

import java.util.UUID;

@Data
@Entity
@Table(name = "character_sheet")
public class CharacterSheetEntity extends RabbitBaseEntity<CharacterSheetListingDTO<SessionEntity>, CharacterSheetListingDTO<SessionEntity>> {

    @Column(name = "id", columnDefinition = "uuid")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "character_name", columnDefinition = "varchar(100)", nullable = false)
    private String characterName;

    @Column(name = "player_id", columnDefinition = "uuid", nullable = false)
    private UUID playerId;

    @Column(name = "trpg_system", nullable = false, columnDefinition = "varchar(45)")
    @Enumerated(EnumType.STRING)
    private ETRPGSystem trpgSystem;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = SessionEntity.class)
    @JoinColumn(name = "session_id", referencedColumnName = "id")
    private SessionEntity session;

    @Override
    public CharacterSheetListingDTO<SessionEntity> toListDTO(EventProducerInterface producer) {
        return new CharacterSheetListingDTO<>(
                getId(),
                getCharacterName(),
                getTrpgSystem(),
                getSession() != null ? getSession().toListDTO(null) : null
        );
    }

    @Override
    public CharacterSheetListingDTO<SessionEntity> toDetailDTO(EventProducerInterface producer) {
        throw new NotImplementedException();
    }

    @Override
    public CharacterSheetListingDTO<SessionEntity> toListDTO() {
        throw new NotImplementedException();
    }

    @Override
    public CharacterSheetListingDTO<SessionEntity> toDetailDTO() {
        throw new NotImplementedException();
    }
}
