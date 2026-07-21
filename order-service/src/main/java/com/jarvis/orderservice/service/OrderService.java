package com.jarvis.orderservice.service;

import com.jarvis.orderservice.dto.CreateOrderRequest;

public interface OrderService {

    void createOrder(CreateOrderRequest createOrderRequest);

}
