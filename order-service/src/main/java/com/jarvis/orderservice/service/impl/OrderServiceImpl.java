package com.jarvis.orderservice.service.impl;

import com.jarvis.orderservice.dto.CreateOrderRequest;
import com.jarvis.orderservice.entity.Order;
import com.jarvis.orderservice.entity.OutboxEvents;
import com.jarvis.orderservice.jpa.repository.OrderRepository;
import com.jarvis.orderservice.jpa.repository.OutboxRepsitory;
import com.jarvis.orderservice.service.OrderService;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private OutboxRepsitory outboxRepsitory;

    public OrderServiceImpl(OrderRepository orderRepository, OutboxRepsitory outboxRepsitory) {
        this.orderRepository = orderRepository;
        this.outboxRepsitory = outboxRepsitory;
    }

    @Override
    @Transactional
    public void createOrder(CreateOrderRequest createOrderRequest) {
        System.out.println("creating order ...");
        try {
            Order order = Order.builder()
                    .id(UUID.randomUUID())
                    .customerId(createOrderRequest.getCustomerId())
                    .totalAmount(createOrderRequest.getTotalAmount())
                    .status("CREATED")
                    .build();
            orderRepository.save(order);

            System.out.println("creating outbox event ...");
            OutboxEvents outboxEvent = OutboxEvents.builder()
                    .id(UUID.randomUUID())
                    .aggregateType("Order")
                    .aggregateId(order.getId())
                    .eventType("OrderCreated")
                    .payload("{\"orderId\":\"" + order.getId() + "\", \"customerId\":\"" + order.getCustomerId() + "\", \"totalAmount\":" + order.getTotalAmount() + ", \"status\":\"" + order.getStatus() + "\"}")
                    .published(false)
                    .build();
            outboxRepsitory.save(outboxEvent);
        } catch (Exception e) {
            System.out.println("Error creating order: " + e.getMessage());
            throw e;
        }


    }
}
