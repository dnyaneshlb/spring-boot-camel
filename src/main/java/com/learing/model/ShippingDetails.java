package com.learing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingDetails {
    String instructions;

    boolean isGift;

    Address deliveryAddress;
}
