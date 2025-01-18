package com.hbsites.rpgtracker.application.resource;

import com.hbsites.commons.domain.params.DefaultParams;
import com.hbsites.rpgtracker.application.service.adapters.PermissionServiceAdapter;
import com.hbsites.rpgtracker.domain.model.UserInfo;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import java.util.List;

@Path("/core/api/permissions")
@RolesAllowed("user")
public class PermissionResource {

    @Inject
    private PermissionServiceAdapter permissionServiceAdapter;

    @GET
    public Uni<UserInfo> getUserPermissions(@BeanParam DefaultParams params) {
        return permissionServiceAdapter.getUserPermission(params);
    }
}
