package com.hbsites.rpgtracker.core.entity;

import com.hbsites.hbsitescommons.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "character_sheet")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class CharacterSheetEntity<LISTDTO, DETAILDTO, SESSIONLISTDTO, SESSIONDETAILDTO> extends BaseEntity<LISTDTO, DETAILDTO> {

    @Column(name = "character_sheet_id", columnDefinition = "uuid")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "character_name", columnDefinition = "varchar(100)", nullable = false)
    private String characterName;

    @Column(name = "player_id", columnDefinition = "uuid", nullable = false)
    private UUID playerId;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = SessionEntity.class)
    @JoinColumn(name = "session_id", referencedColumnName = "session_id")
    private SessionEntity<SESSIONLISTDTO, SESSIONDETAILDTO> session;
}
