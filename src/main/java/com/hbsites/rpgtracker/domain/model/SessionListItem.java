package com.hbsites.rpgtracker.domain.model;

import com.hbsites.commons.rpgtracker.domain.enumeration.ETRPGSystem;

import java.util.UUID;

public record SessionListItem(String slug, String description, boolean dmed, ETRPGSystem system) {}
