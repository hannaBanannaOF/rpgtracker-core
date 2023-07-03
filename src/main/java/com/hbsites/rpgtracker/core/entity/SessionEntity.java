package com.hbsites.rpgtracker.core.entity;

import com.hbsites.hbsitescommons.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "session")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class SessionEntity<LISTDTO, DETAILDTO> extends BaseEntity<LISTDTO, DETAILDTO> {

    @Id
    @GeneratedValue
    @Column(name = "session_id", columnDefinition = "uuid")
    private UUID sessionId;

    @Column(name = "session_name", columnDefinition = "varchar(100)", nullable = false)
    private String sessionName;

    @Column(name = "dm_id", columnDefinition = "uuid", nullable = false)
    private UUID dmId;

    @Column(name = "in_play", columnDefinition = "boolean")
    private Boolean inPlay;

    @OneToMany(mappedBy = "session", fetch = FetchType.LAZY)
    private List<CharacterSheetEntity> sheets;
}
