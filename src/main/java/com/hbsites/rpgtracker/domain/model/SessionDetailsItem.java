package com.hbsites.rpgtracker.domain.model;

import com.hbsites.commons.domain.model.BasicListItem;

import java.util.List;

public record SessionDetailsItem(String slug, String sessionName, List<BasicListItem<String>> characterSheets) { }
