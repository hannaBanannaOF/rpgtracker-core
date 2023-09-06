package com.hbsites.rpgtracker.core.listener;

import com.hbsites.hbsitescommons.dto.SessionBasicInfoDTO;
import com.hbsites.hbsitescommons.messages.SessionBasicInfoDTOListPayload;
import com.hbsites.hbsitescommons.messages.UUIDListPayload;
import com.hbsites.hbsitescommons.queues.RabbitQueues;
import com.hbsites.rpgtracker.core.service.SessionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SessionBasicInfoRequestListener {

    @Lazy
    @Autowired
    SessionService sessionService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitQueues.CORE_SESSION_REQUEST_QUEUE)
    public void process(UUIDListPayload message) {
        List<SessionBasicInfoDTO> infos = sessionService.getInfoById(message.uuids());
        SessionBasicInfoDTOListPayload build = new SessionBasicInfoDTOListPayload(infos, message.userRequested(), message.session(), message.microservice());
        rabbitTemplate.convertSendAndReceiveAsType(RabbitQueues.RPGTRACKER_COC_EXCHANGE, RabbitQueues.COC_SESSION_RESPONSE_QUEUE, build, new ParameterizedTypeReference<>(){});
    }

}
