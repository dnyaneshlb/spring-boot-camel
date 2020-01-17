package com.learing;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MyRoute extends RouteBuilder
{
    @Override
    public void configure() throws Exception {
        from("file:/D:/Personal/docs/source/")
                .to("file:/D:/Personal/docs/dest/");
    }
}
