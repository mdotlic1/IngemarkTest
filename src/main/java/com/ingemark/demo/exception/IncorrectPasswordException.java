package com.ingemark.demo.exception;

public class IncorrectPasswordException extends RuntimeException{
    public IncorrectPasswordException(String message){super(message);}
}
