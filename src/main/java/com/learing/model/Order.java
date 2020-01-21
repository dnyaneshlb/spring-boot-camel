package com.learing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    List<Product> products;

    ShippingDetails shippingDetails;

    Double cost;

    Instant orderPlacedTime;

    Integer status;
}
