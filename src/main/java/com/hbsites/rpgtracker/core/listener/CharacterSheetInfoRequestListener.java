package com.hbsites.rpgtracker.core.listener;

import com.hbsites.hbsitescommons.rpgtracker.dto.CharacterSheetBasicInfoDTO;
import com.hbsites.hbsitescommons.rpgtracker.messages.CharacterSheetBasicInfoDTOListPayload;
import com.hbsites.hbsitescommons.commons.messages.UUIDListPayload;
import com.hbsites.hbsitescommons.commons.queues.RabbitQueues;
import com.hbsites.rpgtracker.core.service.CharacterSheetService;
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
public class CharacterSheetInfoRequestListener {

    @Lazy
    @Autowired
    CharacterSheetService characterSheetService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitQueues.CORE_CHARACTER_SHEET_REQUEST_QUEUE)
    public void process(UUIDListPayload message) {
        log.info("[CHARACTER-SHEET-REQUEST] - Received message: %s".formatted(message.toString()));
        List<CharacterSheetBasicInfoDTO> infos = characterSheetService.getInfoById(message.uuids());

        //This is the message to be returned by the server
        CharacterSheetBasicInfoDTOListPayload build = new CharacterSheetBasicInfoDTOListPayload(infos, message.userRequested(), message.microservice());
        log.info("[CHARACTER-SHEET-REQUEST] - Sending message to RabbitMQ (%s:%s): %s".formatted(RabbitQueues.RPGTRACKER_COC_EXCHANGE, RabbitQueues.COC_CHARACTER_SHEET_RESPONSE_QUEUE, build.toString()));
        rabbitTemplate.convertSendAndReceiveAsType(RabbitQueues.RPGTRACKER_COC_EXCHANGE, RabbitQueues.COC_CHARACTER_SHEET_RESPONSE_QUEUE, build, new ParameterizedTypeReference<>(){});

    }

}
