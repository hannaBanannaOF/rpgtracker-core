package com.hbsites.rpgtracker.domain.model;

import com.hbsites.commons.rpgtracker.domain.enumeration.ETRPGSystem;

import java.time.LocalDateTime;
import java.util.UUID;

public record NextSessionItem(String slug, String name, ETRPGSystem system, LocalDateTime date, boolean dmed, Integer scheduleId) { }
