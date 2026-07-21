package com.jarvis.orderservice.jpa.repository;

import com.jarvis.orderservice.entity.OutboxEvents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxRepsitory extends JpaRepository<OutboxEvents, Long> {
}
