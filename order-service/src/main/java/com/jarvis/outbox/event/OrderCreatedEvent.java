package com.jarvis.outbox.event;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvent {

    private UUID eventId;
    private String eventType;
    private String payload;
    private int version;
    private LocalDateTime occurredAt;

}
