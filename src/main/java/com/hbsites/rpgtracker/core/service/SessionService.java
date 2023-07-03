package com.hbsites.rpgtracker.core.service;

import com.hbsites.hbsitescommons.dto.UUIDListPayload;
import com.hbsites.hbsitescommons.dto.UserDTO;
import com.hbsites.hbsitescommons.dto.UserDTOListPayload;
import com.hbsites.hbsitescommons.utils.UserUtils;
import com.hbsites.rpgtracker.core.config.RabbitMQConfig;
import com.hbsites.rpgtracker.core.dto.BasicSessionListingDTO;
import com.hbsites.rpgtracker.core.entity.CharacterSheetEntity;
import com.hbsites.rpgtracker.core.entity.SessionEntity;
import com.hbsites.rpgtracker.core.repository.SessionRepository;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SessionService {

    @Lazy
    @Autowired
    private SessionRepository sessionRepository;
    @Lazy
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public List<BasicSessionListingDTO> getDMedSessions() {
        return sessionRepository.findAllByDmId(UUID.fromString("9af40220-4bf3-4e42-90f6-9f24c449aac4"))
                .stream()
                .map(e -> {
                    BasicSessionListingDTO dto = e.toListDTO();
                    // Populate player list
                    List<UUID> playerIds = e.getSheets().stream().map(CharacterSheetEntity::getPlayerId).toList();
                    HashSet<UUID> playerIdsUnique = new HashSet<UUID>(playerIds);

                    Message newMsg = MessageBuilder.withBody(SerializationUtils.serialize(new UUIDListPayload(playerIdsUnique.stream().toList()))).build();
                    Message result = rabbitTemplate.sendAndReceive(RabbitMQConfig.USER_EXCHANGE, RabbitMQConfig.USER_REQUEST_QUEUE, newMsg);
                    if (result != null) {
                        // To get message sent correlationId
                        String correlationId = newMsg.getMessageProperties().getCorrelationId();

                        // Get response header information
                        HashMap<String, Object> headers = (HashMap<String, Object>) result.getMessageProperties().getHeaders();
                        // Access server Message returned id
                        String msgId = (String) headers.get("spring_returned_message_correlation");
                        if (msgId.equals(correlationId)) {
                            UserDTOListPayload users = (UserDTOListPayload) SerializationUtils.deserialize(result.getBody());
                            dto.setPlayers(playerIdsUnique.stream().map(uuid ->
                                    users.getUsers().stream().filter(us -> us.getUuid().equals(uuid)).findFirst().orElse(new UserDTO()).getDisplayName()
                            ).collect(Collectors.toList()));
                        }
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

}
