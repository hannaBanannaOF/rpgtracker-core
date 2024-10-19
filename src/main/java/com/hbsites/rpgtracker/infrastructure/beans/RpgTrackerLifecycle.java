package com.hbsites.rpgtracker.infrastructure.beans;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbsites.commons.domain.enumeration.EMicroservice;
import com.hbsites.commons.infrastructure.messages.gateway.GatewayUpdatePaths;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.NotRegisteredException;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;
import com.orbitz.consul.model.health.ServiceHealth;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.reactive.messaging.rabbitmq.OutgoingRabbitMQMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@ApplicationScoped
public class RpgTrackerLifecycle {

    private final UUID instanceId = UUID.randomUUID();

    @Channel("update-paths")
    Emitter<GatewayUpdatePaths> emitter;
//
//    @Inject
//    Consul consulClient;

    void onStart(@Observes StartupEvent ev) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(this::updateGatewayPaths, 5000, TimeUnit.MILLISECONDS);
        //            HealthClient healthClient = consulClient.healthClient();
        //            List<ServiceHealth> instances = healthClient.getHealthyServiceInstances("rpgtracker-core").getResponse();
        //            instanceId = "rpgtracker-core-"+instances.size();
        //            ImmutableRegistration reg = ImmutableRegistration.builder()
        //                    .id(instanceId)
        //                    .name("rpgtracker-core")
        //                    .address("127.0.0.1")
        //                    .port(Integer.parseInt(System.getProperty("quarkus.http.port")))
        //                    .putMeta("version", getClass().getPackage() != null ? getClass().getPackage().getImplementationVersion() != null ? getClass().getPackage().getImplementationVersion() : "DEVELOPMENT"  : "DEVELOPMENT")
        //                    .build();
        //            consulClient.agentClient().register(reg);
        //            LOGGER.info("Instance registered: id={}", reg.getId());
    }

    void onStop(@Observes ShutdownEvent ev) {
//        consulClient.agentClient().deregister(instanceId);
//        LOGGER.info("Instance de-registered: id={}", instanceId);
    }

    private void updateGatewayPaths() {
        GatewayUpdatePaths paths = GatewayUpdatePaths.builder()
                .instance("rpgtracker-core-1")
                .url("http://localhost:8080")
                .pathRegex("/core/**")
                .build();
        emitter.send(paths);
        log.info("Gateway paths updated!");
    }

}
