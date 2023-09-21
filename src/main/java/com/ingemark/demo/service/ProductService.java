package com.ingemark.demo.service;

import com.ingemark.demo.model.Product;
import com.ingemark.demo.dto.ProductInputDTO;
import com.ingemark.demo.exception.CodeAlreadyExistsException;
import com.ingemark.demo.exception.ProductNotFoundException;
import com.ingemark.demo.repository.ProductRepository;
import com.ingemark.demo.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final ExchangeRateService exchangeRateService;

    public ProductService(ProductRepository productRepository, ExchangeRateService exchangeRateService) {
        this.productRepository = productRepository;
        this.exchangeRateService = exchangeRateService;
    }

    public Product saveProduct(ProductInputDTO productInputDTO) {
        validateProductInput(productInputDTO);
        if (productRepository.findByCode(productInputDTO.getCode()).isPresent()) {
            logger.error(Constants.CODE_ALREADY_EXISTS_LOG_MESSAGE, productInputDTO.getCode());
            throw new CodeAlreadyExistsException(Constants.CODE_ALREADY_EXISTS_EXCEPTION_MESSAGE);
        }
        logger.info(Constants.SAVING_PRODUCTS_LOG_MESSAGE, productInputDTO.getCode());
        Product product = new Product();
        return createProduct(productInputDTO, product);
    }

    public List<Product> findAll() {
        logger.info(Constants.FETCHING_PRODUCTS_LOG_MESSAGE);
        return productRepository.findAll();
    }

    public Page<Product> findAll(Pageable pageable) {
        logger.info(Constants.FETCHING_PRODUCTS_LOG_MESSAGE);
        return productRepository.findAll(pageable);
    }

    public Product findById(Long id) {
        logger.info(Constants.FINDING_PRODUCT_LOG_MESSAGE, id);
        return productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error(Constants.PRODUCT_NOT_FOUND_LOG_MESSAGE, id);
                    return new ProductNotFoundException(Constants.PRODUCT_NOT_FOUND_EXCEPTION_MESSAGE + id);
                });
    }

    public Product updateProduct(Long id, ProductInputDTO productInputDTO) {
        validateProductInput(productInputDTO);

        productRepository.findByCode(productInputDTO.getCode()).ifPresent(existingProduct -> {
            if (!existingProduct.getId().equals(id)) {
                logger.error(Constants.CODE_ALREADY_EXISTS_LOG_MESSAGE, productInputDTO.getCode());
                throw new CodeAlreadyExistsException(Constants.CODE_ALREADY_EXISTS_EXCEPTION_MESSAGE);
            }
        });

        logger.info(Constants.UPDATING_PRODUCT_LOG_MESSAGE, id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error(Constants.PRODUCT_NOT_FOUND_LOG_MESSAGE, id);
                    return new ProductNotFoundException(Constants.PRODUCT_NOT_FOUND_EXCEPTION_MESSAGE + id);
                });
        return createProduct(productInputDTO, product);
    }


    public void deleteProduct(Long id) {
        logger.info(Constants.DELETING_PRODUCT_LOG_MESSAGE, id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error(Constants.PRODUCT_NOT_FOUND_LOG_MESSAGE, id);
                    return new ProductNotFoundException(Constants.PRODUCT_NOT_FOUND_EXCEPTION_MESSAGE + id);
                });
        productRepository.delete(product);
    }

    private Product createProduct(ProductInputDTO productInputDTO, Product product) {
        logger.debug(Constants.CREATING_PRODUCT_LOG_MESSAGE, productInputDTO.getCode());
        product.setCode(productInputDTO.getCode());
        product.setName(productInputDTO.getName());
        product.setPriceEur(productInputDTO.getPriceEur());
        product.setDescription(productInputDTO.getDescription());
        product.setAvailable(productInputDTO.isAvailable());

        product.setPriceUsd(exchangeRateService.calculateUsdPrice(productInputDTO.getPriceEur()));
        return productRepository.save(product);
    }

    private void validateProductInput(ProductInputDTO productInputDTO) {
        if (productInputDTO.getCode().length() != 10) {
            logger.error(Constants.CODE_LENGTH_WARNING);
            throw new IllegalArgumentException(Constants.CODE_LENGTH_WARNING);
        }
        if (productInputDTO.getPriceEur().compareTo(BigDecimal.ZERO) <= 0) {
            logger.error(Constants.PRICE_WARNING);
            throw new IllegalArgumentException(Constants.PRICE_WARNING);
        }
    }

}
