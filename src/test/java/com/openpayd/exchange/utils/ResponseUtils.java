package com.openpayd.exchange.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openpayd.exchange.model.AmountConversion;
import com.openpayd.exchange.model.Currency;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;

public class ResponseUtils {

    public static ObjectMapper mapper = new ObjectMapper();
    public static final String MOCKS_PATH = "/mock/response/";

    public static ResponseEntity<Currency> getMockResponseForRate() throws IOException {
        InputStream inJson = Currency.class.getResourceAsStream(MOCKS_PATH + "currencyResponse.json");
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return new ResponseEntity<>(mapper.readValue(inJson, Currency.class), HttpStatus.ACCEPTED);
    }

    public static ResponseEntity<AmountConversion> getMockResponseForConversion() throws IOException {
        InputStream inJson = Currency.class.getResourceAsStream(MOCKS_PATH + "amountConversion.json");
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return new ResponseEntity<>(mapper.readValue(inJson, AmountConversion.class), HttpStatus.ACCEPTED);
    }

}
