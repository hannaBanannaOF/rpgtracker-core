package com.hbsites.rpgtracker.domain.model;

import com.hbsites.commons.domain.model.BasicListItem;
import com.hbsites.commons.rpgtracker.domain.enumeration.ETRPGSystem;

import java.util.List;

public record SessionDetailsItem(String slug, String sessionName, List<BasicListItem<String>> characterSheets,
                                 Boolean dmed, Integer id, ETRPGSystem system, Boolean inPlay) { }
