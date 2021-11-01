package com.openpayd.exchange.service;

import com.openpayd.exchange.model.Rate;

public interface IRateService {
    Rate getRate(String source, String target);
}
