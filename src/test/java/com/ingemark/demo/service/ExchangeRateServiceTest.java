package com.ingemark.demo.service;

import com.ingemark.demo.exception.ExchangeRateFetchException;
import com.ingemark.demo.model.HnbResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExchangeRateServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testFetchUsdExchangeRateReturnsCachedRate() {
        HnbResponse response = new HnbResponse();
        response.setExchangeRate("1.5");
        when(restTemplate.getForObject(anyString(), eq(HnbResponse[].class)))
                .thenReturn(new HnbResponse[]{response});

        BigDecimal firstResult = exchangeRateService.fetchUsdExchangeRate();
        assertEquals(new BigDecimal("1.5"), firstResult);

        reset(restTemplate);

        BigDecimal secondResult = exchangeRateService.fetchUsdExchangeRate();
        assertEquals(new BigDecimal("1.5"), secondResult);
        verify(restTemplate, never()).getForObject(anyString(), any());
    }

    @Test
    void testFetchUsdExchangeRateFetchesNewRate() {
        HnbResponse response = new HnbResponse();
        response.setExchangeRate("1.5");
        when(restTemplate.getForObject(anyString(), eq(HnbResponse[].class)))
                .thenReturn(new HnbResponse[]{response});

        BigDecimal result = exchangeRateService.fetchUsdExchangeRate();

        assertEquals(new BigDecimal("1.5"), result);
    }

    @Test
    void testFetchUsdExchangeRateHandlesNullResponse() {
        when(restTemplate.getForObject(anyString(), eq(HnbResponse[].class)))
                .thenReturn(null);

        assertThrows(ExchangeRateFetchException.class, () -> exchangeRateService.fetchUsdExchangeRate());
    }

    @Test
    void testCalculateUsdPrice() {
        HnbResponse response = new HnbResponse();
        response.setExchangeRate("10");
        when(restTemplate.getForObject(anyString(), eq(HnbResponse[].class)))
                .thenReturn(new HnbResponse[]{response});

        BigDecimal eurPrice = BigDecimal.valueOf(2);
        BigDecimal result = exchangeRateService.calculateUsdPrice(eurPrice);

        assertEquals(BigDecimal.valueOf(20.0), result);
    }
}
