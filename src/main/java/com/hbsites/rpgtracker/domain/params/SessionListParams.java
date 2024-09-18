package com.hbsites.rpgtracker.domain.params;

import com.hbsites.commons.domain.params.DefaultParams;
import com.hbsites.rpgtracker.domain.enumeration.DmedFilter;
import jakarta.ws.rs.DefaultValue;
import lombok.Getter;
import org.jboss.resteasy.reactive.RestQuery;

@Getter
public class SessionListParams extends DefaultParams {
    @RestQuery
    @DefaultValue("ALL")
    DmedFilter dmedFilter = DmedFilter.ALL;
}
