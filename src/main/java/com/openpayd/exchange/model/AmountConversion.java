package com.openpayd.exchange.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AmountConversion {
    private String date;
    private double result;
}
