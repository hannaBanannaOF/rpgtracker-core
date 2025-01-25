package com.hbsites.rpgtracker.domain.model;

import com.hbsites.commons.rpgtracker.domain.enumeration.ETRPGSystem;

public record CharacterSheetDetailsItem(String slug, String characterName, Integer id, ETRPGSystem system, Boolean played) { }
