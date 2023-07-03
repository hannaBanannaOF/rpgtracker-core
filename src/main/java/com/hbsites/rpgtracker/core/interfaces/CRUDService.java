package com.hbsites.rpgtracker.core.interfaces;

import java.util.List;

public interface CRUDService<IDCLASS, DTO, CREATEDTO, DETAILDTO> {

    List<DTO> getAll();
    DETAILDTO create(CREATEDTO dto);

    void deleteById(IDCLASS id);

    DETAILDTO getById(IDCLASS id);

    DETAILDTO update(IDCLASS id, CREATEDTO payload);

}
