package com.ingemark.demo.controller;

import com.ingemark.demo.dto.ProductInputDTO;
import com.ingemark.demo.model.Product;
import com.ingemark.demo.service.ProductService;
import com.ingemark.demo.util.Constants;
import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private Bucket rateLimitBucket;

    @Mock
    private Pageable pageable;

    @InjectMocks
    private ProductController productController;

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
    void testGetProductsRateLimitExceeded() {
        when(rateLimitBucket.tryConsume(Constants.GET_LIST_TOKEN_COST)).thenReturn(false);
        ResponseEntity<Page<Product>> response = productController.getProducts(pageable);
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
    }

    @Test
    void testGetProductsSuccess() {
        when(rateLimitBucket.tryConsume(Constants.GET_LIST_TOKEN_COST)).thenReturn(true);

        List<Product> products = Collections.singletonList(new Product());
        Page<Product> productPage = new PageImpl<>(products, PageRequest.of(0, 10), products.size());

        when(productService.findAll(any(Pageable.class))).thenReturn(productPage);

        ResponseEntity<Page<Product>> response = productController.getProducts(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), Constants.RESPONSE_BODY_NOT_NULL);
        assertEquals(1, response.getBody().getNumberOfElements());
    }


    @Test
    void testGetProductByIdRateLimitExceeded() {
        when(rateLimitBucket.tryConsume(Constants.GET_ID_TOKEN_COST)).thenReturn(false);
        ResponseEntity<Product> response = productController.getProductById(1L);
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
    }

    @Test
    void testGetProductByIdSuccess() {
        when(rateLimitBucket.tryConsume(Constants.GET_ID_TOKEN_COST)).thenReturn(true);
        Product product = new Product();
        when(productService.findById(1L)).thenReturn(product);
        ResponseEntity<Product> response = productController.getProductById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody());
    }

    @Test
    void testCreateProductRateLimitExceeded() {
        when(rateLimitBucket.tryConsume(Constants.POST_TOKEN_COST)).thenReturn(false);
        ResponseEntity<Product> response = productController.createProduct(new ProductInputDTO());
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
    }

    @Test
    void testCreateProductSuccess() {
        when(rateLimitBucket.tryConsume(Constants.POST_TOKEN_COST)).thenReturn(true);
        Product product = new Product();
        when(productService.saveProduct(any())).thenReturn(product);
        ResponseEntity<Product> response = productController.createProduct(new ProductInputDTO());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody());
    }

    @Test
    void testUpdateProductRateLimitExceeded() {
        when(rateLimitBucket.tryConsume(Constants.PUT_TOKEN_COST)).thenReturn(false);
        ResponseEntity<Product> response = productController.updateProduct(1L, new ProductInputDTO());
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
    }

    @Test
    void testUpdateProductSuccess() {
        when(rateLimitBucket.tryConsume(Constants.PUT_TOKEN_COST)).thenReturn(true);
        Product product = new Product();
        when(productService.updateProduct(anyLong(), any())).thenReturn(product);
        ResponseEntity<Product> response = productController.updateProduct(1L, new ProductInputDTO());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody());
    }

    @Test
    void testDeleteProductRateLimitExceeded() {
        when(rateLimitBucket.tryConsume(Constants.DELETE_TOKEN_COST)).thenReturn(false);
        ResponseEntity<Void> response = productController.deleteProduct(1L);
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
    }

    @Test
    void testDeleteProductSuccess() {
        when(rateLimitBucket.tryConsume(Constants.DELETE_TOKEN_COST)).thenReturn(true);
        doNothing().when(productService).deleteProduct(1L);
        ResponseEntity<Void> response = productController.deleteProduct(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
