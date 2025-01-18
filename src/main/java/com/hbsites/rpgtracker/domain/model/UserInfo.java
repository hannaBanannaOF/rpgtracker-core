package com.hbsites.rpgtracker.domain.model;

import java.util.List;

public record UserInfo(SideMenu sideMenu, List<Permission> permissions) {
    public record SideMenu(List<MenuItem> menu){
        public record MenuItem(String label, String iconLib, String iconName, String menuLink, List<MenuItem> subMenu, List<ActiveCheck> activechecks){
            public record ActiveCheck(Boolean exact, String path){}
        }
    }
    public record Permission(String screen, List<String> permission){}
}
