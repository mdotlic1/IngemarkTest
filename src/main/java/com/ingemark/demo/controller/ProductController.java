package com.ingemark.demo.controller;

import com.ingemark.demo.model.Product;
import com.ingemark.demo.service.ProductService;
import com.ingemark.demo.util.Constants;
import com.ingemark.demo.dto.ProductInputDTO;
import io.github.bucket4j.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;
    private final Bucket rateLimitBucket;

    public ProductController(ProductService productService, Bucket rateLimitBucket){
        this.productService = productService;
        this.rateLimitBucket = rateLimitBucket;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<Page<Product>> getProducts(@PageableDefault(size = 10) Pageable pageable) {
        if (rateLimitBucket.tryConsume(Constants.GET_LIST_TOKEN_COST)) {
            Page<Product> products = productService.findAll(pageable);
            return ResponseEntity.ok(products);
        } else {
            logger.warn(Constants.RATE_LIMIT_EXCEEDED_LOG_MESSAGE);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(null);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        if (rateLimitBucket.tryConsume(Constants.GET_ID_TOKEN_COST)) {
            return ResponseEntity.ok(productService.findById(id));
        } else {
            logger.warn(Constants.RATE_LIMIT_EXCEEDED_LOG_MESSAGE);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(null);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductInputDTO productInput) {
        if (rateLimitBucket.tryConsume(Constants.POST_TOKEN_COST)) {
            return ResponseEntity.ok(productService.saveProduct(productInput));
        } else {
            logger.warn(Constants.RATE_LIMIT_EXCEEDED_LOG_MESSAGE);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(null);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductInputDTO productInputDTO) {
        if (rateLimitBucket.tryConsume(Constants.PUT_TOKEN_COST)) {
            return ResponseEntity.ok(productService.updateProduct(id, productInputDTO));
        } else {
            logger.warn(Constants.RATE_LIMIT_EXCEEDED_LOG_MESSAGE);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(null);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (rateLimitBucket.tryConsume(Constants.DELETE_TOKEN_COST)) {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn(Constants.RATE_LIMIT_EXCEEDED_LOG_MESSAGE);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(null);
        }
    }
}
