package com.hbsites.rpgtracker.core.producer;

import com.hbsites.hbsitescommons.commons.enumeration.EMicroservice;
import com.hbsites.hbsitescommons.commons.interfaces.EventProducerInterface;
import com.hbsites.hbsitescommons.commons.messages.UUIDListPayload;
import com.hbsites.hbsitescommons.commons.queues.RabbitQueues;
import com.hbsites.hbsitescommons.commons.utils.UserUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Log4j2
public class UserRequestProducer implements EventProducerInterface {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public void getFromRabbitMQ(List<UUID> uuids, UUID sessionId, UUID characterSheet) {
        UUIDListPayload newMsg = new UUIDListPayload(uuids, UserUtils.getUserUUID(), sessionId, EMicroservice.RPGTRACKER_CORE, characterSheet);
        log.info("[USER-REQUEST] - Sending message to RabbitMQ(%s:%s): %s".formatted(RabbitQueues.USER_EXCHANGE, RabbitQueues.USER_REQUEST_QUEUE, newMsg.toString()));
        rabbitTemplate.convertAndSend(RabbitQueues.USER_EXCHANGE, RabbitQueues.USER_REQUEST_QUEUE, newMsg);
    }
}
