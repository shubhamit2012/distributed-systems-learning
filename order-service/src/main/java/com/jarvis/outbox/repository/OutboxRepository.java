package com.jarvis.outbox.repository;

import com.jarvis.outbox.entity.OutboxEvents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OutboxRepository extends JpaRepository<OutboxEvents, UUID> {

    List<OutboxEvents> findTop100ByPublishedFalseOrderByCreatedAtAsc();

}
