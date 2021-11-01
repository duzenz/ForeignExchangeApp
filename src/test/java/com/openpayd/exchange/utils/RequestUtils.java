package com.openpayd.exchange.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openpayd.exchange.model.Exchange;

import java.io.IOException;
import java.io.InputStream;

public class RequestUtils {

    public static ObjectMapper mapper = new ObjectMapper();
    public static final String MOCKS_PATH = "/mock/request/";

    public static String getConversionPost() throws IOException {
        InputStream inJson = Exchange.class.getResourceAsStream(MOCKS_PATH + "exchange.json");
        return mapper.writeValueAsString(mapper.readValue(inJson, Exchange.class));
    }

}
