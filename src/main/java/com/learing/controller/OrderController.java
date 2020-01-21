package com.learing.controller;

import com.learing.model.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @GetMapping("/user/{orderId}")
    public ResponseEntity<Order> getOrderDetails(@PathVariable("orderId") String orderId){
        return new ResponseEntity<Order>(new Order(), HttpStatus.OK);
    }

}
