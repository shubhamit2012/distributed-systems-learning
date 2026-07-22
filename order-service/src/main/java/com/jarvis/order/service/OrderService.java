package com.jarvis.order.service;

import com.jarvis.order.dto.CreateOrderRequest;

public interface OrderService {

    void createOrder(CreateOrderRequest createOrderRequest);

}
