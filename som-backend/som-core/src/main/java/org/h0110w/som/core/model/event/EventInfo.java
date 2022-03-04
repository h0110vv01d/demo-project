package org.h0110w.som.core.model.event;

import lombok.Data;

/**
 * dto that contains some specific data about an event, which is stored in the database as json
 */
@Data
public class EventInfo {
    private Long issuerId;
    private Long targetId;
    private String message;
}
