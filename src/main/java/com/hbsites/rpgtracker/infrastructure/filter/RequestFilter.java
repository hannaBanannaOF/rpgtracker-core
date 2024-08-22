package com.hbsites.rpgtracker.infrastructure.filter;

import com.hbsites.commons.infrastructure.config.ProviderManager;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;

public class RequestFilter {

    @Inject
    private ProviderManager providerManager;

    @ServerRequestFilter(priority = Priorities.AUTHORIZATION + 1, preMatching = true)
    public Uni<Void> populateProvider() {
        return providerManager.populate();
    }
}
