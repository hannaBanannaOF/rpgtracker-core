package com.hbsites.rpgtracker.infrastructure.database.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;
import org.seasar.doma.jdbc.entity.NamingType;

import java.time.LocalDateTime;

@Entity(metamodel = @Metamodel, naming = NamingType.SNAKE_LOWER_CASE)
@Table(name = "session_calendar")
public record SessionCalendarEntity(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) Integer id,
                                    Integer sessionId,
                                    LocalDateTime sessionDate)  {}
