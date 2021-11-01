package com.openpayd.exchange.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Exchange {
    private String source;
    private String target;
    private Double amount;
}
