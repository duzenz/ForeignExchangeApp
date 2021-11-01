package com.openpayd.exchange.service;

import com.openpayd.exchange.entity.Conversion;
import com.openpayd.exchange.exceptions.ApiRuntimeException;
import com.openpayd.exchange.exceptions.ResourceNotFoundException;
import com.openpayd.exchange.model.AmountConversion;
import com.openpayd.exchange.model.Exchange;
import com.openpayd.exchange.repository.ConversionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ConversionServiceTest {

    @InjectMocks
    private ConversionService conversionService;

    @Mock
    private CurrencyProviderService currencyProviderService;

    @Mock
    private ConversionRepository conversionRepository;

    @Test
    public void getConversionListById() {
        when(conversionRepository.findById(anyLong())).thenReturn(getMockConversionById());
        List<Conversion> conversionList = conversionService.getConversionList(1L, null, null, null);
        assertEquals(1, conversionList.size());
        assertEquals(1000.0, conversionList.get(0).getAmount(), 0000.1);
    }

    @Test
    public void getConversionListByDate() {
        when(conversionRepository.findByCreatedDate(any(Date.class), any(Pageable.class)))
                .thenReturn(getMockConversionsByDate());
        List<Conversion> conversionList = conversionService.getConversionList(null, new Date(), null, null);
        assertEquals(2, conversionList.size());
        assertEquals(1000.0, conversionList.get(0).getAmount(), 0000.1);
        assertEquals(2000.0, conversionList.get(1).getAmount(), 0000.1);
    }

    @Test
    public void convert() {
        when(currencyProviderService.getAmountConversion(anyString(), anyString(), anyDouble())).thenReturn(getMockAmountConversion());
        when(conversionRepository.save(any(Conversion.class))).thenReturn(getMockConversionById().get());
        Conversion conversion = conversionService.convert(getMockExchange());
        assertEquals(100.0, conversion.getAmount(), 000.1);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void convertWithEmptyResponse() {
        when(currencyProviderService.getAmountConversion(anyString(), anyString(), anyDouble())).thenReturn(Optional.empty());
        conversionService.convert(getMockExchange());
    }

    @Test(expected = ApiRuntimeException.class)
    public void getRateWithException() {
        when(currencyProviderService.getAmountConversion(anyString(), anyString(), anyDouble())).thenThrow(ApiRuntimeException.class);
        conversionService.convert(getMockExchange());
    }

    private Exchange getMockExchange() {
        Exchange exchange = new Exchange();
        exchange.setAmount(100.0);
        exchange.setSource("TRY");
        exchange.setTarget("USD");
        return exchange;
    }

    private Optional<Conversion> getMockConversionById() {
        Conversion conversion = new Conversion();
        conversion.setTarget("TRY");
        conversion.setSource("USD");
        conversion.setAmount(1000.0);
        conversion.setCreatedDate(new Date());
        conversion.setId(1L);
        return Optional.of(conversion);
    }

    private Page<Conversion> getMockConversionsByDate() {
        Conversion conversion1 = new Conversion();
        conversion1.setTarget("TRY");
        conversion1.setSource("USD");
        conversion1.setAmount(1000.0);
        conversion1.setCreatedDate(new Date());
        conversion1.setId(1L);
        Conversion conversion2 = new Conversion();
        conversion2.setTarget("TRY");
        conversion2.setSource("USD");
        conversion2.setAmount(2000.0);
        conversion2.setCreatedDate(new Date());
        conversion2.setId(2L);

        List<Conversion> conversionList = new ArrayList<>();
        conversionList.add(conversion1);
        conversionList.add(conversion2);

        return new PageImpl<>(conversionList);
    }

    private Optional<AmountConversion> getMockAmountConversion() {
        AmountConversion amountConversion = new AmountConversion();
        amountConversion.setDate("2021-01-01");
        amountConversion.setResult(100.0);
        return Optional.of(amountConversion);
    }
}
