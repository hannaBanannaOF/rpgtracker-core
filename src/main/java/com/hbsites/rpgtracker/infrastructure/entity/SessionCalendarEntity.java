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

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(listener = BaseEntityListener.class, metamodel = @Metamodel)
@Table(name = "session_calendar")
@Getter
@Setter
public class SessionCalendarEntity extends BaseEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "session_id")
    private UUID sessionId;

    @Column(name = "session_date")
    private LocalDateTime sessionDate;

}
