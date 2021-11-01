package com.openpayd.exchange.service;

import com.openpayd.exchange.entity.Conversion;
import com.openpayd.exchange.model.Exchange;

import java.util.Date;
import java.util.List;

public interface IConversionService {

    List<Conversion> getConversionList(Long transactionId, Date transactionDate,
                                       Integer pageNumber, Integer pageSize);

    Conversion convert(Exchange exchange);
}
