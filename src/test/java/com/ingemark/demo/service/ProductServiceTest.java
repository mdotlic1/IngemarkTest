package com.ingemark.demo.service;

import com.ingemark.demo.model.Product;
import com.ingemark.demo.repository.ProductRepository;
import com.ingemark.demo.dto.ProductInputDTO;
import com.ingemark.demo.exception.CodeAlreadyExistsException;
import com.ingemark.demo.exception.ProductNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private ProductService productService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        when(exchangeRateService.calculateUsdPrice(any(BigDecimal.class))).thenReturn(BigDecimal.TEN);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testSaveProductWithExistingCode() {
        ProductInputDTO inputDTO = new ProductInputDTO();
        inputDTO.setCode("ABCDEFGH10");
        inputDTO.setPriceEur(BigDecimal.TEN);

        when(productRepository.findByCode("ABCDEFGH10")).thenReturn(Optional.of(new Product()));

        assertThrows(CodeAlreadyExistsException.class, () -> productService.saveProduct(inputDTO));
    }

    @Test
    void testSaveProductInvalidCodeLength() {
        ProductInputDTO input = new ProductInputDTO();
        input.setCode("ABC");
        input.setPriceEur(BigDecimal.ONE);

        assertThrows(IllegalArgumentException.class, () -> productService.saveProduct(input));
    }

    @Test
    void testSaveProductInvalidPriceEur() {
        ProductInputDTO input = new ProductInputDTO();
        input.setCode("ABCDEFGH10");
        input.setPriceEur(BigDecimal.ZERO);

        assertThrows(IllegalArgumentException.class, () -> productService.saveProduct(input));
    }

    @Test
    void testFindByIdNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.findById(1L));
    }

    @Test
    void testUpdateProductNotFound() {
        ProductInputDTO inputDTO = new ProductInputDTO();
        inputDTO.setCode("ABCDEFGH10");
        inputDTO.setPriceEur(BigDecimal.TEN);

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(1L, inputDTO));
    }

    @Test
    void testUpdateProductInvalidCodeLength() {
        Product product = new Product();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductInputDTO input = new ProductInputDTO();
        input.setCode("XYZ");
        input.setPriceEur(BigDecimal.ONE);

        assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(1L, input));
    }

    @Test
    void testUpdateProductInvalidPriceEur() {
        Product product = new Product();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductInputDTO input = new ProductInputDTO();
        input.setCode("XYZABC1234");
        input.setPriceEur(BigDecimal.ZERO);

        assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(1L, input));
    }

    @Test
    void testDeleteProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L));
    }
}
