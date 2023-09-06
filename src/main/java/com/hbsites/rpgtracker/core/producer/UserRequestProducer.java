package com.hbsites.rpgtracker.core.producer;

import com.hbsites.hbsitescommons.enumeration.EMicroservice;
import com.hbsites.hbsitescommons.interfaces.EventProducerInterface;
import com.hbsites.hbsitescommons.messages.UUIDListPayload;
import com.hbsites.hbsitescommons.queues.RabbitQueues;
import com.hbsites.hbsitescommons.utils.UserUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class UserRequestProducer implements EventProducerInterface {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public void getFromRabbitMQ(List<UUID> uuids, UUID sessionId, UUID characterSheet) {
        UUIDListPayload newMsg = new UUIDListPayload(uuids, UserUtils.getUserUUID(), sessionId, EMicroservice.RPGTRACKER_CORE, characterSheet);
        rabbitTemplate.convertAndSend(RabbitQueues.USER_EXCHANGE, RabbitQueues.USER_REQUEST_QUEUE, newMsg);
    }
}
