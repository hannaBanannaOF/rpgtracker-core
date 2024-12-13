package com.hbsites.rpgtracker.infrastructure.beans;

import com.hbsites.commons.infrastructure.messages.gateway.GatewayUpdatePaths;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@ApplicationScoped
public class RpgTrackerLifecycle {

    @Channel("update-paths")
    Emitter<GatewayUpdatePaths> emitter;

    void onStart(@Observes StartupEvent ev) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(this::updateGatewayPaths, 5000, TimeUnit.MILLISECONDS);
    }

    void onStop(@Observes ShutdownEvent ev) {

    }

    private void updateGatewayPaths() {
        emitter.send(new GatewayUpdatePaths("rpgtracker-core", "http://host.docker.internal:8080", "/core/**"));
        log.info("Gateway paths updated!");
    }

}
