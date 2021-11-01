package com.openpayd.exchange.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openpayd.exchange.exceptions.ApiRuntimeException;
import com.openpayd.exchange.model.AmountConversion;
import com.openpayd.exchange.model.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class CurrencyProviderService {

    private final Logger logger = LoggerFactory.getLogger(CurrencyProviderService.class);
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Value("${provider.api.url}")
    private String apiUrl;

    @Value("${provider.api.accessKey}")
    private String accessKey;

    public CurrencyProviderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<Currency> getRate(String source, String target) {
        try {
            ResponseEntity<Currency> response = restTemplate.getForEntity(apiUrl + "/latest?access_key=" + accessKey + "&base=" + source + "&symbols=" + target, Currency.class);
            if (response.getBody() != null) {
                logger.debug("Get rate response: {}", response.getBody());
                return Optional.of(objectMapper.convertValue(response.getBody(), Currency.class));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRuntimeException("get.rate.exception", "Get rate failed", e);
        }
        return Optional.empty();
    }

    public Optional<AmountConversion> getAmountConversion(String source, String target, double amount) {
        try {
            ResponseEntity<AmountConversion> response = restTemplate.getForEntity(apiUrl + "/convert?access_key=" + accessKey + "&from=" + source + "&to=" + target + "&amount=" + amount, AmountConversion.class);
            if (response.getBody() != null) {
                logger.debug("Get rate response: {}", response.getBody());
                return Optional.of(objectMapper.convertValue(response.getBody(), AmountConversion.class));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRuntimeException("get.amount.conversion.exception", "Get amount conversion failed", e);
        }
        return Optional.empty();
    }
}
