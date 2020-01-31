package com.learing.model;

import com.baeldung.springsoap.gen.GetCountryRequest;

public class GetCountryRequestBuilder {
    public GetCountryRequest getContryByName(String name) {
        GetCountryRequest req = new GetCountryRequest();
        req.setName(name);
        return req;
    }
}
