package com.learing.service.filter;

import com.learing.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderFilterService {

    public Exchange filterOrderResult(Exchange exchange) {
        Order order = exchange.getIn().getBody(Order.class);
        order.getShippingDetails().setDeliveryAddress(null);
        log.info("Filtering order and resetting address.");
        log.debug("Setting address API to null");
        return exchange;
    }
}
