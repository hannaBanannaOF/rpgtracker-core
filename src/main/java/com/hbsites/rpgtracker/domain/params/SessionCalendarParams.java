package com.hbsites.rpgtracker.domain.params;

import jakarta.ws.rs.QueryParam;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SessionCalendarParams extends SessionListParams {

    @QueryParam("month")
    private Integer month = LocalDateTime.now().getMonthValue();
}
