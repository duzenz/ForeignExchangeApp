package com.openpayd.exchange.service;

import com.openpayd.exchange.entity.Conversion;
import com.openpayd.exchange.exceptions.ResourceNotFoundException;
import com.openpayd.exchange.model.AmountConversion;
import com.openpayd.exchange.model.Exchange;
import com.openpayd.exchange.repository.ConversionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ConversionService implements IConversionService {
    private final Logger logger = LoggerFactory.getLogger(ConversionService.class);
    private final ConversionRepository conversionRepository;
    private final CurrencyProviderService currencyProviderService;

    public ConversionService(ConversionRepository conversionRepository, CurrencyProviderService currencyProviderService) {
        this.conversionRepository = conversionRepository;
        this.currencyProviderService = currencyProviderService;
    }

    @Override
    public List<Conversion> getConversionList(Long transactionId, Date transactionDate, Integer pageNumber, Integer pageSize) {
        List<Conversion> conversionList = new ArrayList<>();
        if (transactionId != null) {
            logger.debug("Conversion list is called with id");
            Optional<Conversion> conversion = conversionRepository.findById(transactionId);
            conversion.ifPresent(conversionList::add);
        } else if (transactionDate != null) {
            logger.debug("Conversion list is called with date");
            pageNumber = pageNumber != null ? pageNumber : 0;
            pageSize = pageSize != null ? pageSize : 20;
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Conversion> conversionPage = conversionRepository.findByCreatedDate(transactionDate, pageable);
            return conversionPage.toList();
        }
        logger.trace(conversionList.toString());
        return conversionList;
    }

    @Override
    public Conversion convert(Exchange exchange) {
        Optional<AmountConversion> amountConversion = currencyProviderService.getAmountConversion(exchange.getSource(),
                exchange.getTarget(), exchange.getAmount());
        if (amountConversion.isPresent()) {
            Conversion conversion = createConversion(amountConversion.get(), exchange);
            conversionRepository.save(conversion);
            return conversion;
        }
        throw new ResourceNotFoundException("amount.conversion.exception", "Amount conversion not found");
    }

    private Conversion createConversion(AmountConversion amountConversion, Exchange exchange) {
        Conversion conversion = new Conversion();
        conversion.setAmount(amountConversion.getResult());
        conversion.setSource(exchange.getSource());
        conversion.setTarget(exchange.getTarget());
        conversion.setCreatedDate(new Date());
        return conversion;
    }
}
