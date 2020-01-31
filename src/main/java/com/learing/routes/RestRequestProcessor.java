package com.learing.routes;

import com.baeldung.springsoap.gen.GetCountryResponse;
import com.learing.model.GetCountryRequestBuilder;
import com.learing.model.Order;
import com.learing.service.enricher.OrderEnricherService;
import com.learing.service.filter.OrderFilterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.cxf.message.MessageContentsList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RestRequestProcessor extends RouteBuilder {

    private String contextPath = "/camel";

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private OrderEnricherService enricher;

    @Autowired
    private OrderFilterService filterService;

    public RestRequestProcessor() {
    }


    @Override
    public void configure() throws Exception {
        CamelContext context = new DefaultCamelContext();

        //Rest declaration for endpoints
        restConfiguration()
                .contextPath(contextPath)
                .port(serverPort)
                .enableCORS(true)
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Test REST API using Camel")
                .apiContextRouteId("doc-api")
                .component("servlet")
                .bindingMode(RestBindingMode.json);

        configureHelloWorldAPI();
        configureCreateOrderAPI();
    }


    private void configureCreateOrderAPI(){
        //the rest route
        rest()
                .id("create-order-api")
                .consumes("application/json")
                .post("/api/orders")
                .bindingMode(RestBindingMode.json)
                .type(Order.class)
                .to("direct:remoteService");

        //create intermediate route
        from("direct:remoteService")
                .routeId("direct-route")
                .tracing()
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        enricher.enrichOrder(exchange);
                    }
                })
                .setHeader("intercepted-request", constant(200))
                .to("direct:legacyPostOrderAPI");

        //create intermediate route
        log.info("Route Channel legacyPostOrderAPI in action.");
        from("direct:legacyPostOrderAPI")
                .routeId("legacyPostOrderAPI")
                .tracing()
                .process((exchange) -> {
                    //This stands for the API call to Legacy IKEA Systems
                    // The outcome of this call is chained to another camel route
                    // for further processing and eventually we return response to user.
                })
                .to("direct:filterOrderResult");

        //create final route
        log.info("Route Channel filterOrderResult in action.");
        from("direct:filterOrderResult")
                .routeId("filterOrderResult")
                .tracing()
                .process((exchange) -> {
                    filterService.filterOrderResult(exchange);
                });
    }

    private void configureHelloWorldAPI() {
        rest().get("/hello")
                .to("direct:hello");

        //Simple hello world API
        from("direct:hello")
                .setBody(constant("Spain"))
                .bean(GetCountryRequestBuilder.class)
                .setHeader(CxfConstants.OPERATION_NAME, constant("getCountry"))
                .to("cxf://http://localhost:8080/ws?wsdlURL=/wsdl/countries.wsdl" +
                        "&serviceClass=com.baeldung.springsoap.gen.CountriesPort")
                .log("The Population is : ${body[0].getCountry().getPopulation()}")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        System.out.println(exchange);
                        MessageContentsList list = (MessageContentsList) exchange.getIn().getBody();
                        if (list != null && list.size() > 0) {
                            GetCountryResponse response = (GetCountryResponse) list.get(0);
                            exchange.getIn().setBody(response.getCountry());
                        }
                    }
                })
                .setHeader("intercepted-request", constant(200));


    }
}
