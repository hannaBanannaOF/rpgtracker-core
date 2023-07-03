package com.hbsites.rpgtracker.core.entity;

import com.hbsites.hbsitescommons.dto.UserDTO;
import com.hbsites.hbsitescommons.dto.UserDTOListPayload;
import com.hbsites.hbsitescommons.entity.BaseEntity;
import com.hbsites.hbsitescommons.enumeration.ETRPGSystem;
import com.hbsites.rpgtracker.core.config.RabbitMQConfig;
import com.hbsites.rpgtracker.core.dto.BasicSessionListingDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "session")
public class SessionEntity extends BaseEntity<BasicSessionListingDTO, BasicSessionListingDTO> {

    @Id
    @GeneratedValue
    @Column(name = "session_id", columnDefinition = "uuid")
    private UUID sessionId;

    @Column(name = "session_name", columnDefinition = "varchar(100)", nullable = false)
    private String sessionName;

    @Column(name = "dm_id", columnDefinition = "uuid", nullable = false)
    private UUID dmId;

    @Column(name = "in_play", columnDefinition = "boolean")
    private Boolean inPlay;

    @Column(name = "trpg_system", nullable = false, columnDefinition = "varchar(45)")
    @Enumerated(EnumType.STRING)
    private ETRPGSystem trpgSystem;

    @OneToMany(mappedBy = "session", fetch = FetchType.LAZY)
    private List<CharacterSheetEntity> sheets;

    @Override
    public BasicSessionListingDTO toListDTO() {
        return new BasicSessionListingDTO(this.getSessionId(), this.getSessionName(), this.getTrpgSystem(), this.getInPlay(), new ArrayList<String>());
    }

    @Override
    public BasicSessionListingDTO toDetailDTO() {
        throw new NotImplementedException();
    }
}
