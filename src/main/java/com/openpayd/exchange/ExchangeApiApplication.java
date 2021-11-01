package com.openpayd.exchange;

import com.openpayd.exchange.exceptions.RestTemplateResponseErrorHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@SpringBootApplication
public class ExchangeApiApplication {

    @Value("${resttemplate.timeout.seconds:10}")
    private int timeout;

    public static void main(String[] args) {
        //TODO
        try {
            SpringApplication.run(ExchangeApiApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(timeout))
                .setReadTimeout(Duration.ofSeconds(timeout))
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

}
