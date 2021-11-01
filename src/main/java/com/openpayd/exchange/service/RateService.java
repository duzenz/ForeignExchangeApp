package com.openpayd.exchange.service;

import com.openpayd.exchange.exceptions.ResourceNotFoundException;
import com.openpayd.exchange.model.Currency;
import com.openpayd.exchange.model.Rate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RateService implements IRateService {
    private final Logger logger = LoggerFactory.getLogger(RateService.class);
    private final CurrencyProviderService currencyProviderService;

    public RateService(CurrencyProviderService currencyProviderService) {
        this.currencyProviderService = currencyProviderService;
    }

    @Override
    public Rate getRate(String source, String target) {
        Optional<Currency> currency = currencyProviderService.getRate(source, target);
        if (currency.isPresent()) {
            logger.debug("currency response: " + currency.get().toString());
            Rate rate = new Rate();
            rate.setRate(currency.get().getRates().get(target));
            return rate;
        }
        throw new ResourceNotFoundException("currency.not.found", "Rate not found");
    }
}
