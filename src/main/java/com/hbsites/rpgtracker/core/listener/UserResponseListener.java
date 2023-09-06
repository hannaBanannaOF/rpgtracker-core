package com.hbsites.rpgtracker.core.listener;

import com.hbsites.hbsitescommons.enumeration.EMicroservice;
import com.hbsites.hbsitescommons.messages.UserDTOListPayload;
import com.hbsites.hbsitescommons.queues.RabbitQueues;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserResponseListener {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @RabbitListener(queues = RabbitQueues.CORE_USER_RESPONSE_QUEUE)
    public void process(UserDTOListPayload message) {
        simpMessagingTemplate.convertAndSend("/topic/" + message.userRequested().toString() + "/users", message);
    }
}
