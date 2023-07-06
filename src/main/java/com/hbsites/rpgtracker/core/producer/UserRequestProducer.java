package com.hbsites.rpgtracker.core.producer;

import com.hbsites.hbsitescommons.dto.UserDTO;
import com.hbsites.hbsitescommons.interfaces.EventProducerInterface;
import com.hbsites.hbsitescommons.messages.UUIDListPayload;
import com.hbsites.hbsitescommons.messages.UserDTOListPayload;
import com.hbsites.hbsitescommons.queues.RabbitQueues;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class UserRequestProducer implements EventProducerInterface<List<UserDTO>> {

    @Autowired
    RabbitTemplate userRabbitTemplate;

    @Override
    public List<UserDTO> getFromRabbitMQ(List<UUID> uuids) {

        UUIDListPayload newMsg = new UUIDListPayload(uuids);
        UserDTOListPayload result = userRabbitTemplate.convertSendAndReceiveAsType(RabbitQueues.USER_EXCHANGE, RabbitQueues.USER_REQUEST_QUEUE, newMsg, new ParameterizedTypeReference<>(){});
        if (result != null) {
            return result.users();
        }
        return new ArrayList<>();
    }
}
