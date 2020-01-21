package com.learing.service.enricher;

import com.learing.model.Address;
import com.learing.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class OrderEnricherService {

    public Exchange enrichOrder(Exchange exchange) {
        log.info("Enriching order..");
        Order order = exchange.getIn().getBody(Order.class);
        if(order != null) {
            //Set the missing params in the request
            order.setOrderPlacedTime(Instant.now());
            if(order.getShippingDetails() != null &&
                    order.getShippingDetails().getDeliveryAddress() == null) {
                Address address = new Address("Pune", "Baner", "Amar Apex", 412101);
                order.getShippingDetails().setDeliveryAddress(address);
                exchange.getIn().setBody(order);
                return exchange;
            }
        }
        return exchange;
    }
}
