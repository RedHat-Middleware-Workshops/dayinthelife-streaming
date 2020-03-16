package com.legacy.ordersservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping
public class OrdersServiceController {

    @Autowired
    OrdersService service;

    @PostMapping(
        path = "/place", 
        produces = "application/json")
    @ResponseBody
    public String place(@RequestBody Order order) {
        return service.placeOrder(order);
    }
}