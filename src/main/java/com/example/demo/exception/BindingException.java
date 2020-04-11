package com.example.demo.exception;


public class BindingException extends RuntimeException {
    public BindingException() {
    }
    public BindingException(String message) {
        super(message);
    }
    public BindingException(String message, Throwable cause) {
        super(message, cause);
    }
}
