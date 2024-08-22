package com.hbsites.rpgtracker.domain.params;

import com.hbsites.commons.domain.params.DefaultParams;
import jakarta.ws.rs.DefaultValue;
import lombok.Getter;
import org.jboss.resteasy.reactive.RestQuery;

@Getter
public class SessionListParams extends DefaultParams {
    @RestQuery
    @DefaultValue("ALL")
    DmedFilter dmedFilter = DmedFilter.ALL;

    public enum DmedFilter {
        ALL, ONLY_PLAYER, ONLY_DM
    }
}
