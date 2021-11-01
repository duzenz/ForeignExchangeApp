package com.openpayd.exchange.controller;

import com.openpayd.exchange.model.Rate;
import com.openpayd.exchange.service.RateService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rates")
public class RateController {

    private final RateService rateService;

    public RateController(RateService rateService) {
        this.rateService = rateService;
    }

    @ApiOperation(value = "Get exchange rate", notes = "Get exchange rate")
    @GetMapping("")
    public ResponseEntity<Rate> getExchangeRate(@RequestParam String source, @RequestParam String target) {
        return ResponseEntity.ok(rateService.getRate(source, target));
    }
}
