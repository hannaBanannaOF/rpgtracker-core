package com.hbsites.rpgtracker.application.service.v1;

import com.hbsites.commons.domain.params.DefaultParams;
import com.hbsites.commons.rpgtracker.domain.enumeration.ETRPGSystem;
import com.hbsites.rpgtracker.application.service.interfaces.PermissionService;
import com.hbsites.rpgtracker.domain.enumeration.DmedFilter;
import com.hbsites.rpgtracker.domain.model.UserInfo;
import com.hbsites.rpgtracker.infrastructure.database.entity.SessionEntity;
import com.hbsites.rpgtracker.infrastructure.repository.interfaces.CharacterSheetRepository;
import com.hbsites.rpgtracker.infrastructure.repository.interfaces.SessionRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class PermissionServiceV1 implements PermissionService {

    @Inject
    private JsonWebToken token;

    @Inject
    private SessionRepository sessionRepository;

    @Override
    public Uni<UserInfo> getUserPermission(DefaultParams params) {

        List<UserInfo.SideMenu.MenuItem> menuItems = new ArrayList<>(List.of(
            new UserInfo.SideMenu.MenuItem("link_home", "tb", "TbHome", "/", List.of(), List.of()),
            new UserInfo.SideMenu.MenuItem("link_sessions", "gi", "GiTabletopPlayers", "/me/sessions", List.of(), List.of()),
            new UserInfo.SideMenu.MenuItem("link_sheets", "gi", "GiScrollUnfurled", "/me/sheets", List.of(), List.of())
        ));

        List<UserInfo.Permission> permissions = new ArrayList<>();
        List<String> roles = token.<JsonObject>getClaim("resource_access")
                .getJsonObject("rpgtracker")
                .getJsonArray("roles")
                .stream().map(JsonValue::toString).toList();

        if (roles.contains("\"player\"")) {
            permissions.add(new UserInfo.Permission("character-sheets", List.of("create")));
        }
        if (roles.contains("\"dm\"")) {
            permissions.add(new UserInfo.Permission("sessions", List.of("create")));
        }

        UUID userId = UUID.fromString(token.getSubject());

        return sessionRepository.findAllByPlayerIdOrDmId(userId, DmedFilter.ONLY_DM).onItem().transform(sessions -> {
            Map<ETRPGSystem, List<SessionEntity>> sessionByTrpgSystem = sessions.stream().collect(Collectors.groupingBy(SessionEntity::trpgSystem));
            if (sessionByTrpgSystem.containsKey(ETRPGSystem.GHOSTBUSTERS)) {
                menuItems.add(new UserInfo.SideMenu.MenuItem("ghostbusters.main", "gi", "GiFloatingGhost", "#", List.of(), List.of()));
                if (sessionByTrpgSystem.get(ETRPGSystem.GHOSTBUSTERS).stream().anyMatch(session -> userId.equals(session.dmId()))) {
                    permissions.add(new UserInfo.Permission("ghostbusters", List.of("create", "update", "delete")));
                }
            }
            return new UserInfo(new UserInfo.SideMenu(menuItems), permissions);
        });
    }
}
