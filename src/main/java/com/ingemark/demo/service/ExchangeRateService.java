package com.ingemark.demo.service;

import com.ingemark.demo.model.HnbResponse;
import com.ingemark.demo.util.Constants;
import com.ingemark.demo.exception.ExchangeRateFetchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class ExchangeRateService {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateService.class);
    private final RestTemplate restTemplate;
    private BigDecimal cachedExchangeRate;
    private LocalDateTime lastFetched;

    public ExchangeRateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public BigDecimal fetchUsdExchangeRate() {
        if (cachedExchangeRate != null && lastFetched != null &&
                Duration.between(lastFetched, LocalDateTime.now()).toMinutes() <= Constants.CACHE_DURATION_MINUTES) {
            logger.debug(Constants.USING_CACHED_RATE_LOG_MESSAGE, cachedExchangeRate);
            return cachedExchangeRate;
        }

        logger.info(Constants.FETCHING_FRESH_RATE_LOG_MESSAGE);

        HnbResponse[] response = restTemplate.getForObject(Constants.getHnbApiUrl("USD"), HnbResponse[].class);

        if (response == null || response.length == 0) {
            logger.error(Constants.FAILED_FETCHING_RATE_LOG_MESSAGE);
            throw new ExchangeRateFetchException(Constants.EXCHANGE_RATE_FETCH_EXCEPTION_MESSAGE);
        }

        String exchangeRate = response[0].getExchangeRate().replace(',', '.');
        cachedExchangeRate = BigDecimal.valueOf(Double.parseDouble(exchangeRate));
        lastFetched = LocalDateTime.now();
        logger.info(Constants.UPDATING_RATE_LOG_MESSAGE, cachedExchangeRate);

        return cachedExchangeRate;
    }

    public BigDecimal calculateUsdPrice(BigDecimal priceEur) {
        logger.debug(Constants.CALCULATING_PRICE_LOG_MESSAGE, priceEur);
        return priceEur.multiply(fetchUsdExchangeRate());
    }
}
