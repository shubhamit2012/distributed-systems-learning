package com.jarvis.outbox.publisher;

import com.jarvis.outbox.entity.OutboxEvents;
import com.jarvis.outbox.repository.OutboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OutboxPublisher {

    private static final Logger log = LoggerFactory.getLogger(OutboxPublisher.class);
    private final OutboxRepository outboxRepository;

    public OutboxPublisher(OutboxRepository outboxRepository) {
        this.outboxRepository = outboxRepository;
    }

    @Scheduled(fixedDelay = 1000)
    public void publish() {
        List<OutboxEvents> outboxEvents = outboxRepository.findTop100ByPublishedFalseOrderByCreatedAtAsc();
        for (OutboxEvents event : outboxEvents) {
            try {
                publish(event);
                event.setPublished(true);
                outboxRepository.save(event);
            } catch (Exception e) {
                log.info("Error publishing event: {}", e.getMessage());
            }
        }
    }

    private void publish(OutboxEvents event) {
        log.info("Publishing event: {} with payload: {}", event.getEventType(), event.getPayload());
    }
}
