package com.legacy.ordersservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdersService {

    @Autowired
    private OrdersRepository repository;

    public String placeOrder(Order order) {
        return repository.placeOrder(order);
    }
}
