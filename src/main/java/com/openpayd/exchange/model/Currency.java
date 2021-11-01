package com.openpayd.exchange.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Currency {
    private String base;
    private String date;
    private Map<String, Double> rates;
}
