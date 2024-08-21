package com.hbsites.rpgtracker.infrastructure.beans;

import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.NotRegisteredException;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;
import com.orbitz.consul.model.health.ServiceHealth;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class RpgTrackerLifecycle {

//    private static final Logger LOGGER = LoggerFactory
//            .getLogger(RpgTrackerLifecycle.class);
//
//    private String instanceId;
//
//    @Inject
//    Consul consulClient;

    void onStart(@Observes StartupEvent ev) {
//        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
//        executor.schedule(() -> {
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
//        }, 5000, TimeUnit.MILLISECONDS);
    }

    void onStop(@Observes ShutdownEvent ev) {
//        consulClient.agentClient().deregister(instanceId);
//        LOGGER.info("Instance de-registered: id={}", instanceId);
    }

}
