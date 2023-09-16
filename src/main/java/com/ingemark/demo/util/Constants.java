package com.ingemark.demo.util;

public class Constants {

    public static final String HNB_API_URL = "https://api.hnb.hr/tecajn-eur/v3?valuta=USD";
    public static final String EXCHANGE_RATE_FETCH_EXCEPTION_MESSAGE = "Failed to fetch the exchange rate from the external API";
    public static final String CODE_ALREADY_EXISTS_EXCEPTION_MESSAGE = "Code already exists in the database";
    public static final String CODE_ALREADY_EXISTS_LOG_MESSAGE = "Product with code {} already exists";
    public static final String PRODUCT_NOT_FOUND_EXCEPTION_MESSAGE = "Product not found for id: ";
    public static final String PRODUCT_NOT_FOUND_LOG_MESSAGE = "Product with ID {} not found";
    public static final String CODE_LENGTH_WARNING = "Code must be exactly 10 characters long";
    public static final String CODE_BLANK_WARNING = "Code cannot be blank";
    public static final String NAME_BLANK_WARNING = "Name cannot be blank";
    public static final String PRICE_WARNING = "Price must be greater than 0.";
    public static final String SAVING_PRODUCTS_LOG_MESSAGE = "Saving product with code: {}";
    public static final String FETCHING_PRODUCTS_LOG_MESSAGE = "Fetching all products";
    public static final String FINDING_PRODUCT_LOG_MESSAGE = "Finding product by ID: {}";
    public static final String UPDATING_PRODUCT_LOG_MESSAGE = "Updating product with ID: {}";
    public static final String DELETING_PRODUCT_LOG_MESSAGE = "Deleting product with ID: {}";
    public static final String CREATING_PRODUCT_LOG_MESSAGE = "Creating/Updating product with code: {}";
    public static final String RATE_LIMIT_EXCEEDED_LOG_MESSAGE = "Rate limit exceeded";
    public static final String USING_CACHED_RATE_LOG_MESSAGE = "Using cached exchange rate: {}";
    public static final String FETCHING_FRESH_RATE_LOG_MESSAGE = "Fetching fresh exchange rate from HNB API";
    public static final String FAILED_FETCHING_RATE_LOG_MESSAGE = "Failed to fetch exchange rate from HNB API";
    public static final String UPDATING_RATE_LOG_MESSAGE = "Updated exchange rate to: {}";
    public static final String CALCULATING_PRICE_LOG_MESSAGE = "Calculating USD price for EUR value: {}";
    public static final String USERNAME_NOT_FOUND = "Username not found: ";
    public static final String OLD_PASSWORD_INCORRECT = "Old password is incorrect";
    public static final String ADMIN_CHANGED_PASSWORD_FOR_USER = "Admin '{}' changed the password for user '{}'";
    public static final String PASSWORD_CHANGE_REQUEST = "Received request to change password for user: {}";
    public static final String PASSWORD_CHANGE_SUCCESS = "Successfully changed password for user: {}";
    public static final String PASSWORD_CHANGE_ERROR = "Error changing password for user: {}. Exception: {}";
    public static final String PASSWORD_CHANGE_UNEXPECTED_ERROR = "Unexpected error changing password for user: {}. Exception: {}";
    public static final String UNEXPECTED_ERROR = "An unexpected error occurred.";
    public static final String CSFR_TOKEN = "csrfToken";
    public static final String MIN_VALUE = "0.01";
    public static final String EXCHANGE_RATE= "srednji_tecaj";
    public static final String ERROR = "error";
    public static final long CACHE_DURATION_MINUTES = 5;
    public static final long GET_LIST_TOKEN_COST = 1;
    public static final long GET_ID_TOKEN_COST = 1;
    public static final long POST_TOKEN_COST = 1;
    public static final long PUT_TOKEN_COST = 1;
    public static final long DELETE_TOKEN_COST = 1;
}
