package com.hbsites.rpgtracker.core.listener;

import com.hbsites.hbsitescommons.commons.messages.UUIDListPayload;
import com.hbsites.hbsitescommons.commons.queues.RabbitQueues;
import com.hbsites.hbsitescommons.rpgtracker.dto.SessionBasicInfoDTO;
import com.hbsites.hbsitescommons.rpgtracker.messages.SessionBasicInfoDTOListPayload;
import com.hbsites.rpgtracker.core.service.SessionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log4j2
public class SessionBasicInfoRequestListener {

    @Lazy
    @Autowired
    SessionService sessionService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitQueues.CORE_SESSION_REQUEST_QUEUE)
    public void process(UUIDListPayload message) {
        log.info("[SESSION-REQUEST] - Received message: %s".formatted(message.toString()));
        List<SessionBasicInfoDTO> infos = sessionService.getInfoById(message.uuids());
        SessionBasicInfoDTOListPayload build = new SessionBasicInfoDTOListPayload(infos, message.userRequested(), message.session(), message.microservice());
        log.info("[SESSION-REQUEST] - Sending message to RabbitMQ (%s:%s): %s".formatted(RabbitQueues.RPGTRACKER_COC_EXCHANGE, RabbitQueues.COC_SESSION_RESPONSE_QUEUE, build.toString()));
        rabbitTemplate.convertSendAndReceiveAsType(RabbitQueues.RPGTRACKER_COC_EXCHANGE, RabbitQueues.COC_SESSION_RESPONSE_QUEUE, build, new ParameterizedTypeReference<>(){});
    }

}
