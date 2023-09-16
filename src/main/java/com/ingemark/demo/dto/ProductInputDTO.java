package com.ingemark.demo.dto;

import com.ingemark.demo.util.Constants;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
public class ProductInputDTO {

    @Size(min = 10, max = 10, message = Constants.CODE_LENGTH_WARNING)
    @NotBlank(message = Constants.CODE_BLANK_WARNING)
    private String code;

    @NotBlank(message = Constants.NAME_BLANK_WARNING)
    private String name;

    @DecimalMin(value = Constants.MIN_VALUE, message = Constants.PRICE_WARNING)
    private BigDecimal priceEur;

    private String description;

    private boolean isAvailable;
}
