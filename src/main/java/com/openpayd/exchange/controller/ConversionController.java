package com.openpayd.exchange.controller;

import com.openpayd.exchange.entity.Conversion;
import com.openpayd.exchange.model.Exchange;
import com.openpayd.exchange.service.ConversionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/conversions")
public class ConversionController {

    private final ConversionService conversionService;

    public ConversionController(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @PostMapping("")
    public ResponseEntity<Conversion> getExchangeRate(@RequestBody Exchange exchange) {
        return new ResponseEntity<>(conversionService.convert(exchange), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<Conversion>> getConversions(@RequestParam(required = false) Long id,
                                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                                           @RequestParam(required = false) Integer page,
                                                           @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(conversionService.getConversionList(id, date, page, size));
    }

}
