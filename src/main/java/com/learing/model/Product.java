package com.learing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    String name;

    String uniqueId;

    String description;

    String category;

    Double cost;
}
