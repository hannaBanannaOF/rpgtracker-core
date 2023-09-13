package com.hbsites.rpgtracker.core.entity;

import com.hbsites.hbsitescommons.dto.UserDTO;
import com.hbsites.hbsitescommons.entity.RabbitBaseEntity;
import com.hbsites.hbsitescommons.enumeration.ETRPGSystem;
import com.hbsites.hbsitescommons.interfaces.EventProducerInterface;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "session")
public class SessionEntity extends RabbitBaseEntity<BasicSessionListingDTO, BasicSessionListingDTO> {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "uuid")
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

    @OneToMany(mappedBy = "session", fetch = FetchType.EAGER, targetEntity = CharacterSheetEntity.class)
    private List<CharacterSheetEntity> sheets;

    @Override
    public BasicSessionListingDTO toListDTO(EventProducerInterface producer) {
        List<String> players = new ArrayList<>();

        if (producer != null) {
            // Populate player list
            List<UUID> playerIds = getSheets().stream().map(CharacterSheetEntity::getPlayerId).toList();
            HashSet<UUID> playerIdsUnique = new HashSet<>(playerIds);

            try {
                producer.getFromRabbitMQ(playerIdsUnique.stream().toList(), this.getSessionId(), null);
            } catch (Exception e) {
                // ignore it
            }
        }

        return new BasicSessionListingDTO(
                this.getSessionId(),
                this.getSessionName(),
                this.getTrpgSystem(),
                this.getInPlay());
    }

    @Override
    public BasicSessionListingDTO toDetailDTO(EventProducerInterface producer) {
        throw new NotImplementedException();
    }

    @Override
    public BasicSessionListingDTO toListDTO() {
        throw new NotImplementedException();
    }

    @Override
    public BasicSessionListingDTO toDetailDTO() {
        throw new NotImplementedException();
    }
}
