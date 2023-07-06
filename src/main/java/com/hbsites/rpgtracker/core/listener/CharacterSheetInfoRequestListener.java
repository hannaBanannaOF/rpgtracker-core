package com.hbsites.rpgtracker.core.listener;

import com.hbsites.hbsitescommons.dto.CharacterSheetBasicInfoDTO;
import com.hbsites.hbsitescommons.messages.CharacterSheetBasicInfoDTOListPayload;
import com.hbsites.hbsitescommons.messages.UUIDListPayload;
import com.hbsites.hbsitescommons.queues.RabbitQueues;
import com.hbsites.rpgtracker.core.config.RabbitMQRPGTrackerCoreConfig;
import com.hbsites.rpgtracker.core.service.CharacterSheetService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CharacterSheetInfoRequestListener {

    @Lazy
    @Autowired
    CharacterSheetService characterSheetService;

    @Autowired
    @Qualifier("coreRabbitTemplate")
    RabbitTemplate coreRabbitTemplate;

    @RabbitListener(queues = RabbitQueues.RPGTRACKER_CORE_CHARACTER_SHEET_REQUEST)
    public void process(UUIDListPayload message) {
        List<CharacterSheetBasicInfoDTO> infos = characterSheetService.getInfoById(message.uuids());

        //This is the message to be returned by the server
        CharacterSheetBasicInfoDTOListPayload build = new CharacterSheetBasicInfoDTOListPayload(infos);
//        CorrelationData correlationData = new CorrelationData(message.getMessageProperties().getCorrelationId());
        coreRabbitTemplate.convertSendAndReceiveAsType(RabbitQueues.RPGTRACKER_CORE_EXCHANGE, RabbitQueues.RPGTRACKER_CORE_CHARACTER_SHEET_RESPONSE, build, new ParameterizedTypeReference<>(){});

    }

}
