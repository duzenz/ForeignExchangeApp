package com.openpayd.exchange.service;

import com.openpayd.exchange.exceptions.ApiRuntimeException;
import com.openpayd.exchange.exceptions.ResourceNotFoundException;
import com.openpayd.exchange.model.Currency;
import com.openpayd.exchange.model.Rate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class RateServiceTest {

    @InjectMocks
    private RateService rateService;

    @Mock
    private CurrencyProviderService currencyProviderService;

    @Test
    public void getRate() {
        when(currencyProviderService.getRate(anyString(), anyString())).thenReturn(getMockCurrency());
        Rate rate = rateService.getRate("USD", "TRY");
        assertEquals(1000.0, rate.getRate(), 0.0001);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getRateWithEmptyResponse() {
        when(currencyProviderService.getRate(anyString(), anyString())).thenReturn(Optional.empty());
        rateService.getRate("USD", "TRY");
    }

    @Test(expected = ApiRuntimeException.class)
    public void getRateWithException() {
        when(currencyProviderService.getRate(anyString(), anyString())).thenThrow(ApiRuntimeException.class);
        rateService.getRate("USD", "TRY");
    }

    private Optional<Currency> getMockCurrency() {
        Currency currency = new Currency();
        currency.setBase("USD");
        currency.setDate("2021-01-01");
        Map<String, Double> map = new HashMap<>();
        map.put("TRY", 1000.0);
        currency.setRates(map);
        return Optional.of(currency);
    }
}
