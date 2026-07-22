package com.jarvis.order.service.impl;

import com.jarvis.order.dto.CreateOrderRequest;
import com.jarvis.order.entity.Order;
import com.jarvis.order.enums.OrderStatus;
import com.jarvis.order.jpa.repository.OrderRepository;
import com.jarvis.order.service.OrderService;
import com.jarvis.outbox.entity.OutboxEvents;
import com.jarvis.outbox.enums.OutboxStatus;
import com.jarvis.outbox.event.OrderCreatedEvent;
import com.jarvis.outbox.publisher.OutboxPublisher;
import com.jarvis.outbox.repository.OutboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepsitory;

    public OrderServiceImpl(OrderRepository orderRepository, OutboxRepository outboxRepsitory) {
        this.orderRepository = orderRepository;
        this.outboxRepsitory = outboxRepsitory;
    }

    @Override
    @Transactional
    public void createOrder(CreateOrderRequest createOrderRequest) {
        log.info("creating order ...");
        Order order = Order.builder()
                .id(UUID.randomUUID())
                .customerId(createOrderRequest.getCustomerId())
                .totalAmount(createOrderRequest.getTotalAmount())
                .status(String.valueOf(OrderStatus.CREATED))
                .createdAt(LocalDateTime.now())
                .build();
        orderRepository.save(order);

        log.info("creating order created event ...");
        OrderCreatedEvent orderCreatedEvent = OrderCreatedEvent.builder()
                .version(1)
                .eventType("OrderCreated")
                .eventId(UUID.randomUUID())
                .payload(order.toString())
                .build();

        log.info("creating outbox event ...");
        OutboxEvents outboxEvent = OutboxEvents.builder()
                .id(UUID.randomUUID())
                .aggregateType("Order")
                .aggregateId(order.getId())
                .eventType("OrderCreated")
                .payload(orderCreatedEvent.toString())
                .published(false)
                .createdAt(LocalDateTime.now())
                .build();
        outboxRepsitory.save(outboxEvent);
    }
}
