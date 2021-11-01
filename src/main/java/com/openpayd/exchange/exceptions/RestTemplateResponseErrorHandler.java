package com.openpayd.exchange.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    private final Logger logger = LoggerFactory.getLogger(RestTemplateResponseErrorHandler.class);

    @Override
    public boolean hasError(ClientHttpResponse httpResponse)
            throws IOException {
        return (httpResponse.getStatusCode().series() == CLIENT_ERROR
                || httpResponse.getStatusCode().series() == SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        logger.error(httpResponse.getStatusText());
        if (httpResponse.getRawStatusCode() == 400 || httpResponse.getRawStatusCode() == 404) {
            throw new HttpClientErrorException(httpResponse.getStatusCode());
        } else if (httpResponse.getRawStatusCode() == 401) {
            throw new HttpServerErrorException(httpResponse.getStatusCode());
        } else {
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, "Unknown error");
        }
    }
}
