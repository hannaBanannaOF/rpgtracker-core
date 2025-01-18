package com.hbsites.rpgtracker.application.service.interfaces;

import com.hbsites.commons.domain.params.DefaultParams;
import com.hbsites.rpgtracker.domain.model.UserInfo;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface PermissionService {
    Uni<UserInfo> getUserPermission(DefaultParams params);
}
