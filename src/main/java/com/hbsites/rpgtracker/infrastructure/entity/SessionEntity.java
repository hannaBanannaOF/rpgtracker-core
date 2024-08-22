package com.hbsites.rpgtracker.infrastructure.entity;

import com.hbsites.commons.infrastructure.database.entity.BaseEntity;
import com.hbsites.commons.infrastructure.database.entitylistener.BaseEntityListener;
import com.hbsites.commons.rpgtracker.domain.enumeration.ETRPGSystem;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;

import java.util.UUID;

@Entity(listener = BaseEntityListener.class, metamodel = @Metamodel)
@Table(name = "session")
@Getter
@Setter
@RegisterForReflection
public class SessionEntity extends BaseEntity {

    @Id
    @Column(name = "id")
    private UUID sessionId;

    @Column(name = "session_name")
    private String sessionName;

    @Column(name = "dm_id")
    private UUID dmId;

    @Column(name = "in_play")
    private Boolean inPlay;

    @Column(name = "trpg_system")
    private ETRPGSystem trpgSystem;

}