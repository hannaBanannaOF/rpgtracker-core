package com.hbsites.rpgtracker.application.service.adapters;

import com.hbsites.commons.domain.params.DefaultParams;
import com.hbsites.commons.domain.service.VersionedService;
import com.hbsites.rpgtracker.application.service.interfaces.PermissionService;
import com.hbsites.rpgtracker.application.service.v1.PermissionServiceV1;
import com.hbsites.rpgtracker.domain.model.UserInfo;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class PermissionServiceAdapter extends VersionedService<PermissionService> implements PermissionService {

    @Inject
    PermissionServiceV1 permissionServiceV1;

    @Override
    protected PermissionService getServiceByApiVersion(int apiVersion) {
        return switch(apiVersion) {
            default -> permissionServiceV1;
        };
    }

    @Override
    public Uni<UserInfo> getUserPermission(DefaultParams params) {
        return getServiceByApiVersion(params.getApiVersion()).getUserPermission(params);
    }
}
