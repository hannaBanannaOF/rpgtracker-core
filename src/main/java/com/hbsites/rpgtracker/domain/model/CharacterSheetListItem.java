package com.hbsites.rpgtracker.domain.model;

import com.hbsites.commons.rpgtracker.domain.enumeration.ETRPGSystem;

public record CharacterSheetListItem (String slug, String description, ETRPGSystem system) { }
