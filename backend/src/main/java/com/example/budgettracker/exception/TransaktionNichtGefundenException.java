package com.example.budgettracker.exception;

public class TransaktionNichtGefundenException extends RuntimeException {

    public TransaktionNichtGefundenException(String message) {
        super(message);
    }
    public TransaktionNichtGefundenException(String message, Throwable cause) {
        super(message, cause);
    }
}
